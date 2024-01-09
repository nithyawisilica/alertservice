package com.track.alerts.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationRequest {

    @JsonProperty("root_org_id")
    private String rootOrgId;
    @JsonProperty("sub_org_id")
    private String subOrgId;
    @JsonProperty("notification_type")
    private Integer messageType;
    @JsonProperty("message")
    private String message;
    @JsonProperty("chmessage")
    private String chMessage;
    @JsonProperty("rule_id")
    private String ruleId;
    @JsonProperty("tag_id")
    private String tagId;
    @JsonProperty("infant_id")
    private String infantId;
    @JsonProperty("rssi")
    private String rssi;
    @JsonProperty("listener_id")
    private String listenerId;
    @JsonProperty("latitude")
    private String latitude;
    @JsonProperty("longitude")
    private String longitude;
    @JsonProperty("infant_name")
    private String infantName;
    @JsonProperty("layer_id")
    private String layerId;
    @JsonProperty("mapLongId")
    private String mapLongId;
    @JsonProperty("priority")
    private int priority;
    @JsonProperty("batteryLevel")
    private String batteryLevel;
    @JsonProperty("alertPriority")
    private String alertPriority;
    @JsonProperty("groupId")
    private String groupId;
    @JsonProperty("listener_name")
    private String listenerName;
    @JsonProperty("infant_room_layer")
    private String infantRoomLayer;
}
