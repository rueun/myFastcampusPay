package com.fastcampuspay.money.adapter.out.service;

import com.fastcampuspay.common.CommonHttpClient;
import com.fastcampuspay.money.application.port.out.GetMembershipPort;
import com.fastcampuspay.money.application.port.out.MembershipStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MemberServiceAdapter implements GetMembershipPort {

    private final CommonHttpClient commonHttpClient;
    private final String memberServiceUrl;

    public MemberServiceAdapter(CommonHttpClient commonHttpClient,
                                @Value("${service.membership.url}") String memberServiceUrl) {
        this.commonHttpClient = commonHttpClient;
        this.memberServiceUrl = memberServiceUrl;
    }

    @Override
    public MembershipStatus getMembership(String membershipId) {

        String url = String.join("/", memberServiceUrl, "membership", membershipId);

        try {
            String jsonResponse = commonHttpClient.sendGetRequest(url).body();

            ObjectMapper objectMapper = new ObjectMapper();
            Membership membership = objectMapper.readValue(jsonResponse, Membership.class);

            if (membership.isValid()) {
                return new MembershipStatus(membership.getMembershipId(), true);
            } else {
                return new MembershipStatus(membership.getMembershipId(), false);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
