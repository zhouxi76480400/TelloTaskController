package org.enes.tellotaskcontroller.objects;

import java.io.Serializable;

public class Task implements Serializable {

    public String command_name;

    public boolean need_extra_value;

    public String extra_value;

    public long create_time;

}
