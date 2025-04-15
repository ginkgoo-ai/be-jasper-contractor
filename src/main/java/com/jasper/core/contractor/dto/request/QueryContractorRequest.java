package com.jasper.core.contractor.dto.request;


import com.jasper.core.contractor.domain.contractor.Contractor;
import com.jasper.core.contractor.jpa.CriteriaBuilderDelegate;
import com.jasper.core.contractor.jpa.query.QueryableRequest;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.flywaydb.core.internal.util.JsonUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryContractorRequest implements QueryableRequest<Contractor> {

    @Nonnull
    private String address;
    @Nullable
    private String city;
    @Nullable
    private String state;
    @Nullable
    private List<String> classifications;
    @Nullable
    private Double radius;


    @Override
    public Predicate[] getPredicate(CriteriaBuilderDelegate<Contractor> builder) {
        List<Predicate> predicates = new ArrayList<>();
        String street = address;
        if(address.indexOf(" ")>0){
            street=address.substring(address.indexOf(" ") + 1);
        }
        predicates.add(builder.when(Contractor::getAddress).like("%" + street + "%"));
        if (StringUtils.isNotBlank(city)) {
            predicates.add(builder.when(Contractor::getCity).eq(city));
        }
        if (StringUtils.isNotBlank(state)) {
            predicates.add(builder.when(Contractor::getState).eq(state));
        }
        if (!CollectionUtils.isEmpty(classifications)) {
            Predicate[] predicatesArray = classifications.stream()
                    .map(it-> builder.when(Contractor::getClassificationArray).jsonContains(JsonUtils.toJson(List.of(it))))
                    .toArray(Predicate[]::new);
            predicates.add(builder.or(predicatesArray));
        }
        return predicates.toArray(Predicate[]::new);
    }
}
