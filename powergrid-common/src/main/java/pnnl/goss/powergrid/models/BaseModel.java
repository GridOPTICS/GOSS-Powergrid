package pnnl.goss.powergrid.models;

/**
 * The base class for all powergrid models and entities.
 *
 * @author Craig Allwardt
 *
 */
public abstract class BaseModel {

    protected String mrid;
    protected String name;

    public String getMrid() {
        return mrid;
    }


    public void setMrid(String mrid) {
        this.mrid = mrid;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public ValidationErrors validate() {
        ValidationErrors errors = new ValidationErrors();

        errors.addIfNullOrEmpty(name, "Invalid name in powergrid model");
        errors.addIfNullOrEmpty(mrid, "Invalid mrid in powergrid model");

        return errors;
    }

}
