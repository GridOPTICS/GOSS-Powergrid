package pnnl.goss.powergrid.models

class ValidationErrors {

    private List<String> errors = new ArrayList<>()

    public Collection<String> getErrors(){
        return errors
    }

    boolean hasErrors(){
        return errors.size() > 0
    }

    def addError(String error){
        errors.add(error)
    }

    def addIfNullOrEmpty(String valueToCheck, String errorMessage){
        if(valueToCheck == null || valueToCheck.isEmpty()){
            errors.add(errorMessage)
        }
    }

}
