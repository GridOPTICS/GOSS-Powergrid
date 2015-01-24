package pnnl.goss.powergrid.parsers

import java.util.List;

import pnnl.goss.powergrid.PowergridCreationReport;

class ParserResultLog implements PowergridCreationReport{

    def warnings = []
    def errors = []

    def warn(String warning) {
        warnings << sprintf("WARN: %s", warning)
    }

    def error(String section, int linenum, String error){
        errors << sprintf("%s: %5d %s", [section, linenum, error])
    }

    @Override
    public List<String> getReport() {
        return warnings + errors;
    }

}
