import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import scripting.*

@RunWith(JUnit4::class)
class PolyScriptEngineTests {

    @Test
    fun `Test Python Script Engine`() {
        Python eval """
            import sys
            print sys
        """.trimIndent()
        Python ["a"] = "Hello from Python"
        Python eval """
            print a
            x = 2 + 2
        """.trimIndent()

        // Using the get() method allows one to obtain the value
        // of a specified variable from the engine instance
        val x = Python["x"]
        Assert.assertEquals(x, 4)
    }

    @Test
    fun `Test Ruby Script Engine`() {
        Ruby eval "x = 1 + 1"
        val x = Ruby["x"]
        Assert.assertEquals(x, 2L)
    }


    @Test
    fun `Test Lua Script Engine`() {
        Lua["x"] = 25
        Lua eval "print('Hello from Luaj')"
        Lua eval "y = math.sqrt(x)"
        Assert.assertEquals(5, Lua["y"])
    }

    @Test
    fun `Test Clojure Script Engine`() {
        Clojure eval "(def x 3)"
        val x = Clojure["x"]
        Assert.assertEquals(3L, x)
    }

    @Test
    fun `Test Javascript Script Engine`() {
        Javascript eval "x = 1 + 1"
        val x = Javascript["x"]
        Assert.assertEquals(2, x)
    }

    @Test
    fun `Lua Access User Data Fields`() {
        Lua["u"] = TestData(1,2)
        Lua eval "x = u.x + u.y"
        Assert.assertEquals(3, Lua["x"])
    }

    @Test
    fun `Lua Access User Data Function`() {
        Lua["u"] = TestData(1,2)
        Lua eval "x = u:add()"
        Assert.assertEquals(3, Lua["x"])
    }

    @Test fun `Python Access User Data Fields`() {
        Python["u"] = TestData(1,2)
        Python eval "x = u.x + u.y"
        Assert.assertEquals(3, Python["x"])
    }

    @Test fun `Python Access User Data Function`() {
        Python["u"] = TestData(1,2)
        Python eval "x = u.add()"
        Assert.assertEquals(3, Python["x"])
    }

    @Test fun `Ruby Access User Data Fields`() {
        Ruby["u"] = TestData(1,2)
        Ruby eval "x = u.x + u.y"
        Assert.assertEquals(3L, Ruby["x"])
    }

    @Test fun `Ruby Access User Data Function`() {
        Ruby["u"] = TestData(1,2)
        Ruby eval "x = u.add()"
        Assert.assertEquals(3L, Ruby["x"])
    }

    @Test fun `Clojure Access User Data Fields`() {
        Clojure["u"] = TestData(1,2)
        Clojure eval "(def x (+ (.x u) (.y u)))"
        Assert.assertEquals(3L, Clojure["x"])
    }

    @Test fun `Clojure Access User Data Function`() {
        Clojure["u"] = TestData(1,2)
        Clojure eval "(def x (.add u))"
        Assert.assertEquals(3, Clojure["x"])
    }

    @Test
    fun `Test Load and Run Python Script`() {
        val (_,e) = PolyScript.create("src/test/resources/test.py")
        Assert.assertNull(e)
    }

    @Test
    fun `Test Load and Run Python Script and Check Result`() {
        val (engine,err) = PolyScript.create("src/test/resources/test2.py")
        Assert.assertNull(err)
        Assert.assertEquals(engine!!["x"], 3)
    }

    @Test
    fun `Test Load and Run Clojure Script and Check Result`() {
        val (engine,err) = PolyScript.create("src/test/resources/test.clj")
        Assert.assertNull(err)
        engine!! eval "(def x (test-add 1 2))"
        Assert.assertEquals(3L, engine["x"])
    }

    @Test
    fun `Test Load and Run Clojure Script and Run with Kotlin values`() {
        val (engine,err) = PolyScript.create("src/test/resources/test.clj")
        Assert.assertNull(err)
        engine!!["y"] = 1L
        engine["z"] = 2L
        engine eval "(def x (test-add y z))"
        Assert.assertEquals(3L, engine["x"])
    }

    @Test
    fun `Test Get Script Object`() {
        Python eval """
            class Hello:
                def __init__(self, name):
                    self.name = name

                def say_name(self):
                    return self.name
        """.trimIndent()
        Python eval "me = Hello('Stewart')"
        val me = Python["me"]
        val name = Python.call(me, "say_name")
        print(name)
        Assert.assertEquals(name, "Stewart")
    }

    class TestData(@JvmField var x: Int, @JvmField var y: Int) {
        fun add(): Int {
            return x + y
        }
    }

}