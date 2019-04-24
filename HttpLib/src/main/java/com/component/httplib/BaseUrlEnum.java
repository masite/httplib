package com.component.httplib;

import com.component.httplib.utils.MockUtils;

/**
 * The type Base url enum.
 *
 * @author haibin.yuan.o on 2017/12/10.
 *         - Description：动态切换baseUrl
 */
public class BaseUrlEnum {

  /**
   * 环境配置
   */
  private static final String DEV         = "dev";
  public static final String UAT         = "uat";
  public static final String TEST        = "test";
//  public static final String STG         = "stg";
  public static final String PROD        = "prod";
  public static final String MOCK        = "mock";

  public static BaseUrlEnums getEnvironmentParamsInfo(String environment) {
    switch (environment) {
      case DEV:
        return BaseUrlEnums.dev;
      case UAT:
        return BaseUrlEnums.uat;
      case TEST:
        return BaseUrlEnums.test;
//      case STG:
//        return BaseUrlEnums.stg;
      case PROD:
        return BaseUrlEnums.prod;
      case MOCK:
        return BaseUrlEnums.mock;
      default:
        break;
    }
    return null;
  }

  /**
   * The enum Base url enums.
   */
  public enum BaseUrlEnums {
    /**
     * The Dev.
     */
    dev {
      @Override
      public String getBaseApiUrl() {
        return "https://toolkitapi-dev.niohome.com/";
      }

//      @Override
//      public String getDomain() {
//        return "toolkitapi-dev.niohome.com";
//      }


      @Override
      public String getMockAPiUrl() {
        return "";
      }


    },
    /**
     * The Uat.
     */
    uat {
      @Override
      public String getBaseApiUrl() {
        return "https://toolkitapi-uat.niohome.com/";
      }

//      @Override
//      public String getDomain() {
//        return "toolkitapi-uat.niohome.com";
//      }


      @Override
      public String getMockAPiUrl() {
        return "";
      }


    },

    /**
     * The Uat.
     */
    test {
      @Override
      public String getBaseApiUrl() {
        return "https://toolkitapi-test.niohome.com/";
      }

//      @Override
//      public String getDomain() {
//        return "toolkitapi-test.niohome.com";
//      }


      @Override
      public String getMockAPiUrl() {
        return "";
      }


    },

    /**
     * The Prod.
     */
    prod {
      @Override
      public String getBaseApiUrl() {
        return "https://toolkitapi.niohome.com/";
      }

//      @Override
//      public String getDomain() {
//        return "toolkitapi.niohome.com";
//      }

      @Override
      public String getMockAPiUrl() {
        return "";
      }


    },
    /**
     * The Uat.
     */
    mock {
      @Override
      public String getBaseApiUrl() {
        return MockUtils.MOCK_HOST;
      }

//      @Override
//      public String getDomain() {
//        return "toolkitapi-test.niohome.com";
//      }

      @Override
      public String getMockAPiUrl() {
        return MockUtils.MOCK_HOST;
      }


    };

    /**
     * Gets base api url.
     *
     * @return the base api url
     */
    public abstract String getBaseApiUrl();

//    public abstract String getDomain();

    public abstract String getMockAPiUrl();


  }
}
