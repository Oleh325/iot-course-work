package ua.lviv.iot.novaPoshtaAPI.model;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuppressFBWarnings
public class Courier extends Item {
    @NotNull(message = "Courier ID shouldn't be null!")
    private Long courierId;
    @NotNull(message = "Courier's department ID shouldn't be null!")
    private Long departmentId;
    @NotNull(message = "Courier's full name shouldn't be null!")
    private String fullName;
    private boolean isWorking;
    private List<Long> parcelIds;

    @Override
    public String getHeaders() {
        return "courierId, departmentId, fullName, "
                + "isWorking, parcelIds";
    }

    @Override
    public String toCSV() {
        return courierId + ", " + departmentId + ", " + fullName + ", "
                + isWorking + ", " + parcelIds;
    }
}
