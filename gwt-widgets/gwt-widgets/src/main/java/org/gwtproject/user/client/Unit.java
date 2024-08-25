//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.gwtproject.user.client;

public enum Unit {
  PX {
    public String getType() {
      return "px";
    }
  },
  PCT {
    public String getType() {
      return "%";
    }
  },
  EM {
    public String getType() {
      return "em";
    }
  },
  EX {
    public String getType() {
      return "ex";
    }
  },
  PT {
    public String getType() {
      return "pt";
    }
  },
  PC {
    public String getType() {
      return "pc";
    }
  },
  IN {
    public String getType() {
      return "in";
    }
  },
  CM {
    public String getType() {
      return "cm";
    }
  },
  MM {
    public String getType() {
      return "mm";
    }
  };

  private Unit() {
  }

  public abstract String getType();
}
