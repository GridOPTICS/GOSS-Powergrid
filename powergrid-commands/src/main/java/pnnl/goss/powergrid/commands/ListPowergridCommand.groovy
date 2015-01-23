package pnnl.goss.powergrid.commands

import org.apache.karaf.shell.console.OsgiCommandSupport
import org.apache.karaf.shell.commands.Command


@Command(scope = "powergrid",
    name = "list", description="List available powergrid models and their original import method.")
class ListPowergridCommand extends OsgiCommandSupport  {

    @Override
    protected Object doExecute() throws Exception {
        println "Executing!"
        return null;
    }

}
