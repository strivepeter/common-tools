package com.peter.common.excel;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * 动态导出EXCEL文档
 *
 * @param <T> 应用泛型，代表任意一个符合javabean风格的类
 *            注意这里为了简单起见，
 *            boolean型的属性xxx的get器方式为getXxx(),
 *            而不是isXxx() byte[]表jpg格式的图片数据
 * @author peter
 * @date 2016-11-11
 */
public class ExportExcelUtil<T> {

    private static Logger log = LoggerFactory.getLogger(ExportExcelUtil.class);

    private final HSSFWorkbook workbook;
    private String dateTimePattern = "yyyy-MM-dd HH:mm:ss";

    public ExportExcelUtil() {
        // 声明一个工作薄
        workbook = new HSSFWorkbook();
    }

    /**
     * 生成excel输出到客户端
     *
     * @param filename excel文件名称
     * @param title    sheet页名称
     * @param mapper   要导出的数据对象与表头的对应关系 key为对象属性 value为列名
     * @param dataset  需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
     *                 javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
     * @param response 客户端相应
     */
    public void export(String filename, String title,
                       LinkedHashMap<String, String> mapper,
                       Collection<T> dataset,
                       HttpServletResponse response) {
        OutputStream out = null;

        try {
            out = response.getOutputStream();
            response.reset();
            response.setHeader("Content-Disposition", "attachment;filename=\""
                    + new String(filename.getBytes("utf-8"), "iso8859-1")
                    + "\"");
            response.setContentType("application/octet-stream; charset=utf-8");
            createSheet(title, mapper, dataset, null, null);
            workbook.write(out);
        } catch (Exception e) {
            log.error("export data failed.", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    log.error("close stream error.", e);
                }
            }

        }
    }

    /**
     * 生成excel输出到客户端
     *
     * @param filename         excel文件名称
     * @param title            sheet页名称
     * @param mapper           要导出的数据对象与表头的对应关系 key为对象属性 value为列名
     * @param dataset          需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
     *                         javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
     * @param headCellStyle    表头样式
     * @param contentCellStyle 数据内容样式
     * @param response         客户端相应
     */
    public void export(String filename, String title,
                       LinkedHashMap<String, String> mapper,
                       Collection<T> dataset,
                       HSSFCellStyle headCellStyle,
                       HSSFCellStyle contentCellStyle,
                       HttpServletResponse response) {
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            response.reset();
            response.setHeader("Content-Disposition", "attachment;filename=\""
                    + new String(filename.getBytes("utf-8"), "iso8859-1")
                    + "\"");
            response.setContentType("application/octet-stream; charset=utf-8");

            createSheet(title, mapper, dataset, headCellStyle, contentCellStyle);
            workbook.write(out);
        } catch (Exception e) {
            log.error("export data failed.", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    log.error("close stream error.", e);
                }
            }

        }
    }

    /**
     * 生成excel输出到指定流文件
     *
     * @param title   sheet页名称
     * @param mapper  要导出的数据对象与表头的对应关系 key为对象属性 value为列名
     * @param dataset 需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
     *                javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
     */
    public void export(String title,
                       LinkedHashMap<String, String> mapper,
                       Collection<T> dataset,
                       OutputStream out) {
        try {
            createSheet(title, mapper, dataset, null, null);
            workbook.write(out);
        } catch (Exception e) {
            log.error("export data failed.", e);
        }
    }

    /**
     * 生成excel输出到指定流文件
     *
     * @param title            sheet页名称
     * @param mapper           要导出的数据对象与表头的对应关系 key为对象属性 value为列名
     * @param dataset          需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
     *                         javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
     * @param headCellStyle    表头样式
     * @param contentCellStyle 数据内容样式
     */
    public void export(String title, LinkedHashMap<String, String> mapper,
                       Collection<T> dataset, HSSFCellStyle headCellStyle,
                       HSSFCellStyle contentCellStyle, OutputStream out) {
        try {
            createSheet(title, mapper, dataset, headCellStyle, contentCellStyle);
            workbook.write(out);
        } catch (Exception e) {
            log.error("export data failed.", e);
        }
    }

    /**
     * 生成excel输出到指定流文件
     *
     * @param out
     */
    public void export(OutputStream out) {
        try {
            workbook.write(out);
        } catch (Exception e) {
            log.error("export data failed.", e);
        }
    }

    /**
     * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据放入HSSFSheet对象中
     *
     * @param title            sheet页名称
     * @param mapper           要导出的数据对象与表头的对应关系 key为对象属性 value为列名
     * @param dataset          需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
     *                         javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
     * @param headCellStyle    表头样式
     * @param contentCellStyle 数据内容样式
     */
    @SuppressWarnings("deprecation")
    public void createSheet(String title, LinkedHashMap<String, String> mapper,
                            Collection<T> dataset, HSSFCellStyle headCellStyle,
                            HSSFCellStyle contentCellStyle) {
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet(title);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth((short) 15);

        if (null == headCellStyle) {
            headCellStyle = getDefaultHeadCellStyle();
        }

        if (null == contentCellStyle) {
            contentCellStyle = getDefaultContentCellStyle();
        }

        // 声明一个画图的顶级管理器
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();

        // 产生表格标题行
        HSSFRow row = sheet.createRow(0);

        Iterator<Entry<String, String>> iterator = mapper.entrySet().iterator();

        List<String> fieldNameList = new ArrayList<String>();

        for (short i = 0; iterator.hasNext(); i++) {
            // 添加表头
            Entry<String, String> entry = iterator.next();
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(headCellStyle);
            HSSFRichTextString text = new HSSFRichTextString(entry.getValue());
            cell.setCellValue(text);
            fieldNameList.add(entry.getKey());
        }

        // 遍历集合数据，产生数据行
        Iterator<T> it = dataset.iterator();
        for (int index = 1; it.hasNext(); index++) {
            row = sheet.createRow(index);
            T t = it.next();
            for (short i = 0; i < fieldNameList.size(); i++) {
                // 添加数据内容
                HSSFCell cell = row.createCell(i);
                cell.setCellStyle(contentCellStyle);

                String fieldName = fieldNameList.get(i);
                String getMethodName = "get"
                        + fieldName.substring(0, 1).toUpperCase()
                        + fieldName.substring(1);
                try {
                    Class<? extends Object> tCls = t.getClass();
                    Method getMethod = tCls.getMethod(getMethodName,
                            new Class[]{});
                    Object value = getMethod.invoke(t, new Object[]{});
                    // 判断值的类型后进行强制类型转换
                    String textValue = null;
                    if (value instanceof Integer) {
                        int intValue = (Integer) value;
                        textValue = String.valueOf(intValue);
                    } else if (value instanceof Float) {
                        float fValue = (Float) value;
                        textValue = String.valueOf(fValue);
                    } else if (value instanceof Double) {
                        double dValue = (Double) value;
                        textValue = String.valueOf(dValue);
                    } else if (value instanceof Long) {
                        long longValue = (Long) value;
                        textValue = String.valueOf(longValue);
                    } else if (value instanceof Date) {
                        Date date = (Date) value;
                        SimpleDateFormat sdf = new SimpleDateFormat(
                                dateTimePattern);
                        textValue = sdf.format(date);
                    } else if (value instanceof byte[]) {
                        // 有图片时，设置行高为60px;
                        row.setHeightInPoints(60);
                        // 设置图片所在列宽度为80px,注意这里单位的一个换算
                        sheet.setColumnWidth(i, (short) (35.7 * 80));
                        byte[] bsValue = (byte[]) value;
                        HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0,
                                1023, 255, (short) 6, index, (short) 6, index);
                        anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_DONT_RESIZE);
                        patriarch.createPicture(anchor, workbook.addPicture(
                                bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));
                    } else if (null != value) {
                        textValue = value.toString();
                    }
                    if (textValue != null) {
                        HSSFRichTextString richString = new HSSFRichTextString(
                                textValue);

                        cell.setCellValue(richString);
                    }
                } catch (Exception e) {
                    log.error("create sheet error.", e);
                }
            }
        }
    }

    /**
     * 默认的表格内容样式
     *
     * @return
     */
    private HSSFCellStyle getDefaultContentCellStyle() {
        HSSFCellStyle style = getStyle();
        style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.LIGHT_YELLOW.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        HSSFFont font = getFont();
        style.setFont(font);
        return style;
    }

    /**
     * 默认表头样式
     *
     * @return
     */
    private HSSFCellStyle getDefaultHeadCellStyle() {
        // 生成一个表头样式
        HSSFCellStyle style = getStyle();
        // 设置这些样式
        style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.SKY_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        // 生成一个字体
        HSSFFont font = getFont();
        font.setColor(HSSFColor.HSSFColorPredefined.VIOLET.getIndex());
        font.setFontHeightInPoints((short) 12);
        // 把字体应用到当前的样式
        style.setFont(font);
        return style;
    }

    public HSSFFont getFont() {
        HSSFFont font = workbook.createFont();

        return font;
    }

    public HSSFCellStyle getStyle() {
        HSSFCellStyle style = workbook.createCellStyle();
        return style;
    }

    /**
     * 如果有时间数据，设定输出格式。默认为"yyyy-MM-dd HH:mm:ss"
     *
     * @param dateTimePattern 时间
     */
    public void setDateTimePattern(String dateTimePattern) {
        this.dateTimePattern = dateTimePattern;
    }

}
