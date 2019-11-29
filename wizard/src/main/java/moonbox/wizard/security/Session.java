
package moonbox.wizard.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Session {

    private Long userId;

    private String user;

    private Long orgId;

    private String org;

}