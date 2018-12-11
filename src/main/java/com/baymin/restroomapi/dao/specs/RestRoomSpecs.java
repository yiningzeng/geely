package com.baymin.restroomapi.dao.specs;

import com.baymin.restroomapi.entity.RestRoom;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * demo
 * https://www.jianshu.com/p/a57cf5166012
 */
@Component
public class RestRoomSpecs {

    public Specification<RestRoom> listSpecsIni(RestRoom conditions) {
        return new Specification<RestRoom>() {
            @Override
            public Predicate toPredicate(Root<RestRoom> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();
                Optional.ofNullable(conditions.getRestRoomName()).ifPresent(v->list.add(cb.like(root.get("restRoomName"), "%"+conditions.getRestRoomName()+"%")));
                Optional.ofNullable(conditions.getAddress()).ifPresent(v->list.add(cb.like(root.get("address"), "%"+conditions.getAddress()+"%")));
                Optional.ofNullable(conditions.getRegion()).ifPresent(v->list.add(cb.like(root.get("region"), "%"+conditions.getRegion()+"%")));
                Optional.ofNullable(conditions.getRemark()).ifPresent(v->list.add(cb.like(root.get("remark"), "%"+conditions.getRemark()+"%")));
//                Optional.ofNullable(conditions.getStatus()).ifPresent(v->list.add(cb.like(root.get("status"), ""+conditions.getStatus())));
//                Optional.ofNullable(conditions.getCarModels()).ifPresent(v->list.add(cb.like(root.get("carModels"), "%"+conditions.getCarModels()+"%")));
                return cb.or(list.toArray(new Predicate[list.size()]));
            }

        };
    }

}
