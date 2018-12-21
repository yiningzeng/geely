package com.baymin.restroomapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.xml.bind.annotation.*;

@XmlRootElement(name="EventNotificationAlert")
//@XmlAccessorType(XmlAccessType.FIELD)
//@XmlType(propOrder = {"id","sequence","ipAddress","regularStr","describe"})
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuckFlow {
    @XmlAttribute
    private String ipAddress;

    @XmlElement(name = "PeopleCounting")
    private Object PeopleCounting;

    @XmlRootElement(name="PeopleCounting")
    @XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class PeopleCounting{
        @XmlAttribute
        private String enter;
    }
}
