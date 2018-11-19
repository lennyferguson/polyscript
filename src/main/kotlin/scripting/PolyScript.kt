package scripting

import java.io.File
import javax.script.Invocable
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager



open class PolyScript(engine: String? = null, suppliedScriptEngine: ScriptEngine? = null) {

    companion object {
        fun create(file: File): EngineResult {
            return ScriptEngineManager().getEngineByExtension(file.extension).let {
                try {
                    it.eval(file.reader())
                    EngineResult(engine = PolyScript(suppliedScriptEngine = it))
                } catch (e: Exception) {
                    EngineResult(error = e)
                }
            }
        }

        fun create(path: String): EngineResult {
            return create(File(path))
        }
    }

    val engine: ScriptEngine by lazy {
        suppliedScriptEngine ?:
        ScriptEngineManager().getEngineByName(engine) ?:
        ScriptEngineManager().getEngineByExtension(engine)
    }

    infix operator fun get(name: String): Any {
        return engine.attr(name)
    }

    operator fun set(name: String, value: Any) {
        engine.put(name, value)
    }

    infix fun eval(script: String): Any? {
        return engine.eval(script)
    }


    fun call(caller: Any, method: String, vararg arg: Any): Any? {
        return (engine as? Invocable)?.let {
            it.invokeMethod(caller, method, *arg)
        }
    }

    fun long(name: String): Long? {
        return numToType(name) { it.toLong() }
    }

    fun int(name: String): Int? {
        return numToType(name) { it.toInt() }
    }

    fun double(name: String): Double? {
        return numToType(name) { it.toDouble() }
    }

    fun float(name: String): Float? {
        return numToType(name) { it.toFloat() }
    }

    private fun <T: Number> numToType(name: String, fn: (Number) -> T): T? {
        return get(name).let {
            when(it) {
                is Number -> fn(it)
                else -> null
            }
        }
    }
}

object Python: PolyScript("python")
object Ruby: PolyScript("jruby")
object Lua: PolyScript("luaj")
object Clojure: PolyScript("Clojure")
object Javascript: PolyScript("javascript")

fun ScriptEngine.attr(name: String): Any {
    return this.context.getAttribute(name)
}

data class EngineResult(val engine: PolyScript? = null, val error: Exception? = null)

fun main(args: Array<String>) {
    val mgr = ScriptEngineManager()
    val factories = mgr.engineFactories

    for (factory in factories) {

        println("ScriptEngineFactory Info")

        val engName = factory.engineName
        val engVersion = factory.engineVersion
        val langName = factory.languageName
        val langVersion = factory.languageVersion

        System.out.printf("\tScript Engine: %s (%s)%n", engName, engVersion)

        val engNames = factory.names
        for (name in engNames) {
            System.out.printf("\tEngine Alias: %s%n", name)
        }

        System.out.printf("\tLanguage: %s (%s)%n", langName, langVersion)

    }
}