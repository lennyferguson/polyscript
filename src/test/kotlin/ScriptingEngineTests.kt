import org.junit.Assert
import scripting.Python
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import scripting.Lua
import scripting.Ruby

@RunWith(JUnit4::class)
class ScriptingEngineTests {

    @Test
    fun `Test Python Script Engine`() {
        Python.engine.eval("")
        Python.engine.eval("import sys")
        Python.engine.eval("print sys")

        // Using the put() method allows one to place values into
        // specified variables within the engine
        Python.engine.put("a", "Hello from Python")

        // As you can see, once the variable has been set with
        // a value by using the put() method, we an issue eval statements
        // to use it.
        Python.engine.eval("print a")
        Python.engine.eval("x = 2 + 2")

        // Using the get() method allows one to obtain the value
        // of a specified variable from the engine instance
        val x = Python.engine.get("x")
        Assert.assertEquals(x, 4)
    }

    @Test
    fun `Test Python Script Engine 2`() {
        Python.engine.eval("")
        Python.engine.eval("import sys")
        Python.engine.eval("print sys")

        // Using the put() method allows one to place values into
        // specified variables within the engine
        Python.engine.put("a", "Hello from Python")

        // As you can see, once the variable has been set with
        // a value by using the put() method, we an issue eval statements
        // to use it.
        Python.engine.eval("print a")
        Python.engine.eval("x = 2 + 2")

        // Using the get() method allows one to obtain the value
        // of a specified variable from the engine instance
        val x = Python.engine.get("x")
        Assert.assertEquals(x, 4)
    }

    @Test
    fun `Test Ruby Script Engine`() {
        val rubyEngine = Ruby.engine
        val context = rubyEngine.context

        rubyEngine.eval("x = 1 + 1")
        val x = rubyEngine.get("x")
        Assert.assertEquals(x, 2L)
    }

    @Test
    fun `Test Ruby Script Engine 2`() {
        val rubyEngine = Ruby.engine
        val context = rubyEngine.context

        rubyEngine.eval("x = 1 + 1")
        val x = rubyEngine.get("x")
        Assert.assertEquals(x, 2L)
    }

    @Test
    fun `Test Lua Script Engine`() {
        val e = Lua.engine
        e.put("x", 25)
        e.eval("print('Hello from Luaj')")
        e.eval("y = math.sqrt(x)")
        Assert.assertEquals(e.get("y"), 5)
    }

    @Test
    fun `Test Lua Script Engine 2`() {
        val e = Lua.engine
        e.put("x", 25)
        e.eval("print('Hello from Luaj')")
        e.eval("y = math.sqrt(x)")
        Assert.assertEquals(e.get("y"), 5)
    }

}