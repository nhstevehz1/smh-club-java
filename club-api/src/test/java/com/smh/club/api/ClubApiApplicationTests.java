package com.smh.club.api;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;

@ActiveProfiles("tests")
@SpringBootTest
@AutoConfigureEmbeddedDatabase(
		provider = ZONKY,
		type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES,
		refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_CLASS)
class ClubApiApplicationTests {

	@Test
	void contextLoads() {
	}

}
