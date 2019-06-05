package com.baymin.restroomapi.ret.model;

import lombok.Data;

import java.util.List;

@Data
public class GasInfo {
    private String status;
    private Header header;
    private Data data;

    @lombok.Data
    public class Header {
        private String deviceId;
        private String funcId;
        private String df;
        private String ea;
        private String fa;
        private String zq;
    }
    @lombok.Data
    public class Data {

        private List<Items> items;
        @lombok.Data
        public class Items {
            private String deviceId;
            private String funcId;
            private Integer df;
            private Float 大厅;
            private Float 男厕;
            private Integer 客流;
            private Float 女厕;
            private Float 无障碍;
            private Float ea;
            private Float fa;
            private Float zq;
            private Integer type;//表示那个位置的
            private String x;
            private List<Items> histroyList;
        }

    }


}
