package scripting

import javax.script.ScriptEngine
import javax.script.ScriptEngineManager

open class ScriptingEngine(engine: String) {
    val engine: ScriptEngine = ScriptEngineManager().getEngineByName(engine)
}

object Python: ScriptingEngine("python")
object Ruby: ScriptingEngine("jruby")
object Lua: ScriptingEngine("luaj")
