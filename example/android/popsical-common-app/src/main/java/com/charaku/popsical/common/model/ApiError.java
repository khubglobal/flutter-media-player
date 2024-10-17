package com.easternblu.khub.common.model;

import com.easternblu.khub.common.Common;
import com.easternblu.khub.common.api.CharakuPathConstant;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * ApiError returned by API endpoint
 * Created by leechunhoe on 22/2/17.
 *
 * Documentation:
 * http://developers.popsical.com
 */
// TODO: Implement CharakuResponse
// TODO: Move to /api/model/ folder
public class ApiError implements Serializable {
    @Expose
    @SerializedName(CharakuPathConstant._ERRORS)
    private Object errors;

    @Expose
    @SerializedName(CharakuPathConstant._ERR_CODE)
    private int errCode;

    public Object getErrors() {
        return errors;
    }

    public String getErrorsString() {
        StringBuilder sb = new StringBuilder();

        if (errors != null) {
            if (errors instanceof String[]) {
                String[] errorsArray = (String[]) errors;
                for (int i=0; i<errorsArray.length; i++) {
                    if (i < errorsArray.length - 1) {
                        sb.append(errorsArray[i] + Common.COMMA);
                    } else {
                        sb.append(errorsArray[i]);
                    }
                }
            } else if (errors instanceof ArrayList) {
                ArrayList errorsArray = (ArrayList) errors;
                for (int i=0; i<errorsArray.size(); i++) {
                    if (errorsArray.get(i) instanceof String) {
                        if (i < errorsArray.size() - 1) {
                            sb.append(errorsArray.get(i) + Common.COMMA);
                        } else {
                            sb.append(errorsArray.get(i));
                        }
                    }
                }
            } else if (errors instanceof String) {
                sb.append((String) errors);
            }
        }

        return sb.toString();
    }

    public void setErrors(String[] errors) {
        this.errors = errors;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }
}
