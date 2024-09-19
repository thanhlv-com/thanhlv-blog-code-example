package com.thanhlv.test.producerRecordPartition;

import java.util.Arrays;

public class PartitionLoad {
    public static void main(String[] args) {
        Integer[] queueSizes = {8, 3, 14};
        run(queueSizes);
    }

    public static void run(Integer[] queueSizes){
        // queueSizes length = max partition, queueSizes là số lượng batch data đang cho gui den partition
        /**
         * ví dụ : Integer[] queueSizes = {8, 3, 14};
         * Có 3 partiotn là 0,1,2
         * partiton 0 có 8 batch đang chờ gửi
         * partiton 1 có 3 batch đang chờ gửi
         * partiton 14 có 8 batch đang chờ gửi
         */
        // lấy về số lớn nhất + 1
        int maxSizePlus1 = queueSizes[0];

        // trong thực tế, length là tham số truyền vào biểu diễn số lượng partition đang có leader.
        int length = queueSizes.length;

        boolean allEqual = true;
        for (int i = 1; i < length; i++) {
            if (queueSizes[i] != maxSizePlus1)
                allEqual = false;
            if (queueSizes[i] > maxSizePlus1)
                maxSizePlus1 = queueSizes[i];
        }
        ++maxSizePlus1;

        //
        if (allEqual) {
            System.out.println("allEqual && queueSizes.length == queueSizes.length");
            return;
        }

        // Invert and fold the queue size, so that they become separator values in the CFT.
        queueSizes[0] = maxSizePlus1 - queueSizes[0];
        for (int i = 1; i < length; i++) {
            queueSizes[i] = maxSizePlus1 - queueSizes[i] + queueSizes[i - 1];
        }
        System.out.println("Output: "+ Arrays.stream(queueSizes).toList());
    }
}
