package pnnl.goss.powergrid.parsers

import java.util.List;

import pnnl.goss.powergrid.PowergridCreationReport;

class ResultLog {
    def debugEnabled = true

    def warnings = []
    def errors = []
    def debug = []
    def ordered = []
    boolean successful = false

    def debug(String message) {
        debug << message
        ordered << [type: 'debug', message: message]
    }
    def warn(String message) {
        warnings << sprintf("WARN: %s", message)
        ordered << [type: 'warn', message: message]
    }

    def error(String message){
        errors << message
        ordered << [type: 'error', message: error]
    }

    def error(String section, int linenum, String error){
        errors << sprintf("%s: %5d %s", [section, linenum, error])
        ordered << [type: 'error', message: error]
    }

    public List<String> getLog() {
        def items = []
        if (debugEnabled){
            ordered.each{ msg ->
                items << sprintf("%6s: %s", msg.type, msg.message)
            }
        }
        else{
            ordered.each{ msg ->
                if (msg != 'debug'){
                    items << sprintf("%6s: %s", msg.type, msg.message)
                }
            }
        }
        items
    }
}
