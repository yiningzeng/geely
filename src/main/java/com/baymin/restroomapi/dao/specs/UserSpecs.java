package com.baymin.restroomapi.dao.specs;

import com.baymin.restroomapi.entity.User;
import com.baymin.restroomapi.utils.Utils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
//import javax.persistence.criteria;

/**
 * demo
 * https://www.jianshu.com/p/a57cf5166012
 */
@Component
public class UserSpecs {


    public Specification<User> userList(User userConditions) {
        return new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();
                if(Utils.stringIsNotNull(userConditions.getUsername())) list.add(cb.like(root.get("username"), "%"+userConditions.getUsername()+"%"));
                if(Utils.stringIsNotNull(userConditions.getDepartment())) list.add(cb.like(root.get("department"), "%"+userConditions.getDepartment()+"%"));
                if(Utils.stringIsNotNull(userConditions.getRelName())) list.add(cb.like(root.get("relName"), "%"+userConditions.getRelName()+"%"));
                return cb.or(list.toArray(new Predicate[list.size()]));
            }

        };
    }

}
