package pwr.isa.backend.RIOT.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AccountDTO {
    String puuid;
    String gameName;
    String tagLine;
}
