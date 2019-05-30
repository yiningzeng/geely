package com.baymin.restroomapi.ret.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class GiveMeFive {

    private String restRoomName;
    private Gas gas;
    private FuckFlow fuckFlow;

    @Data
    public class Gas {
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
                private Float ea;
                private Float fa;
                private Float zq;
                private Integer type;
            }
        }
    }

    @Data
    public static class FuckFlow {
        private Integer today=0;
        private Integer Month=0;
        private Integer all=0;
        private List<Map<String, Object>> todayFlow;
    }
}
