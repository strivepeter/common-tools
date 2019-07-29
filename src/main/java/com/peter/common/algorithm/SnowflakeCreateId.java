package com.peter.common.algorithm;/**
 * Created by DELL on 2019/7/29.
 */

/**
 * @ClassName SnowflakeCreateId
 * @Description 通过Snowflake的算法获取32位的ID（微服务的分库分表）
 * @Author peter
 * @Date 2019/7/29 15:46
 * @Version 1.0
 */
public class SnowflakeCreateId {
    /**
     * workerId ：工作机器ID(0~31)
     * dataCenterId ：数据中心ID(0~31)
     * sequence ：毫秒内序列(0~4095)
     * initialTimestamp ：开始时间截 （2015-01-01）
     * workerIdBits ：机器id所占的位数
     * dataCenterIdBits ：数据标识id所占的位数
     * maxWorkerId ：这个是二进制运算，就是 5 bit最多只能有31个数字，也就是说机器id最多只能是32以内
     * maxDataCenterId ：这个是一个意思，就是 5 bit最多只能有31个数字，机房id最多只能是32以内
     * sequenceBits ：序列在id中占的位数
     * workerIdShift ：机器ID向左移12位
     * dataCenterIdShift ：数据标识id向左移17位(12+5)
     * timestampLeftShift ：时间截向左移22位(5+5+12)
     * sequenceMask ： 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095)
     * lastTimestamp ：上次生成ID的时间截
     */

    private long workerId;
    private long dataCenterId;
    private long sequence;
    private long initialTimestamp = 1288834974657L;
    private long workerIdBits = 5L;
    private long dataCenterIdBits = 5L;
    private long maxWorkerId = -1L ^ (-1L << workerIdBits);
    private long maxDataCenterId = -1L ^ (-1L << dataCenterIdBits);
    private long sequenceBits = 12L;
    private long workerIdShift = sequenceBits;
    private long dataCenterIdShift = sequenceBits + workerIdBits;
    private long timestampLeftShift = sequenceBits + workerIdBits + dataCenterIdBits;
    private long sequenceMask = -1L ^ (-1L << sequenceBits);
    private long lastTimestamp = -1L;


    public SnowflakeCreateId(long workerId, long dataCenterId, long sequence) {
        // sanity check for workerId
        // 这儿不就检查了一下，要求就是你传递进来的机房id和机器id不能超过32，不能小于0
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(
                    String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (dataCenterId > maxDataCenterId || dataCenterId < 0) {
            throw new IllegalArgumentException(
                    String.format("data center Id can't be greater than %d or less than 0", maxDataCenterId));
        }
        System.out.printf(
                "worker starting. timestamp left shift %d, data center id bits %d, worker id bits %d, sequence bits %d, workerId %d",
                timestampLeftShift, dataCenterIdBits, workerIdBits, sequenceBits, workerId);

        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
        this.sequence = sequence;
    }

    public synchronized long nextId() {
        // 这儿就是获取当前时间戳，单位是毫秒
        long timestamp = timeGen();

        if (timestamp < lastTimestamp) {
            System.err.printf("clock is moving backwards.  Rejecting requests until %d.", lastTimestamp);
            throw new RuntimeException(String.format(
                    "clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        if (lastTimestamp == timestamp) {
            // 这个意思是说一个毫秒内最多只能有4096个数字
            // 无论你传递多少进来，这个位运算保证始终就是在4096这个范围内，避免你自己传递个sequence超过了4096这个范围
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0;
        }

        // 这儿记录一下最近一次生成id的时间戳，单位是毫秒
        lastTimestamp = timestamp;

        // 这儿就是将时间戳左移，放到 41 bit那儿；
        // 将机房 id左移放到 5 bit那儿；
        // 将机器 id左移放到 5 bit那儿；将序号放最后12 bit；
        // 最后拼接起来成一个 64 bit的二进制数字，转换成 10 进制就是个 long 型
        return ((timestamp - initialTimestamp) << timestampLeftShift) | (dataCenterId << dataCenterIdShift)
                | (workerId << workerIdShift) | sequence;
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }


    /**
     * ---------------测试---------------
     *
     * @param args
     */
    public static void main(String[] args) {
        SnowflakeCreateId worker = new SnowflakeCreateId(1, 1, 1);
        for (int i = 0; i <= 100; i++) {
            System.out.println(worker.nextId());
        }
    }
}
