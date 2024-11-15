package pwr.isa.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class BackendApplicationTests {

	@Autowired
	private DataSource dataSource;

	@Test
	void testConnection() throws Exception {
		assertNotNull(dataSource.getConnection(), "Database connection should not be null");
	}

}
