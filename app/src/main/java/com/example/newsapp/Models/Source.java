package com.example.newsapp.Models;

import java.io.Serializable;

public class Source  implements Serializable {
    //campos que existem na api e tem que ser o mesmo nome que está na Api
     String id = "";
     String name = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
