package pwr.isa.backend.RIOT.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SummonerDTO {
    @JsonProperty("id")
    String SummonerId;
    String accountId;
    String puuid;
    String name;
    int profileIconId;
    long revisionDate;
    long summonerLevel;
}
