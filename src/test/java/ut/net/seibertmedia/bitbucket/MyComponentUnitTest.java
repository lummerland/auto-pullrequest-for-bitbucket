package ut.net.seibertmedia.bitbucket;

import org.junit.Test;
import net.seibertmedia.bitbucket.api.MyPluginComponent;
import net.seibertmedia.bitbucket.impl.MyPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest
{
    @Test
    public void testMyName()
    {
        MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent",component.getName());
    }
}