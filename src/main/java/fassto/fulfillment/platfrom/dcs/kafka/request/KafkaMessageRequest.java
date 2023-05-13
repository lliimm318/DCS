package fassto.fulfillment.platfrom.dcs.kafka.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class KafkaMessageRequest {

    @NotBlank
    private String action;
    @NotBlank
    private String resourceKey;
    @NotBlank
    private String resource;

    public static KafkaMessageRequest of (String action, String resourceKey, String resource) {
        return KafkaMessageRequest.builder()
                .action(action)
                .resourceKey(resourceKey)
                .resource(resource)
                .build();
    }

}
