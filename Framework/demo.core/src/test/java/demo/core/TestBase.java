package demo.core;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-service-test.xml")
public abstract class TestBase {

	public TestBase() {
		// TODO Auto-generated constructor stub
	}

}
