package com.scy.mykotlinnews.setting.bean

/**
 * 项 目 名: MyKotlinNews
 * 创 建 人: 艺仔加油
 * 创建时间: 2017/11/17 14:27
 * 本类描述:
 */

class Weather {

    var results: List<ResultsBean>? = null

    class ResultsBean {
        /**
         * location : {"id":"WS0E9D8WN298","name":"广州","country":"CN","path":"广州,广州,广东,中国","timezone":"Asia/Shanghai","timezone_offset":"+08:00"}
         * now : {"text":"多云","code":"4","temperature":"25"}
         * last_update : 2017-11-17T14:00:00+08:00
         */

        var location: LocationBean? = null
        var now: NowBean? = null
        var last_update: String? = null

        class LocationBean {
            /**
             * id : WS0E9D8WN298
             * name : 广州
             * country : CN
             * path : 广州,广州,广东,中国
             * timezone : Asia/Shanghai
             * timezone_offset : +08:00
             */

            var id: String? = null
            var name: String? = null
            var country: String? = null
            var path: String? = null
            var timezone: String? = null
            var timezone_offset: String? = null
        }

        class NowBean {
            /**
             * text : 多云
             * code : 4
             * temperature : 25
             */

            var text: String? = null
            var code: String? = null
            var temperature: String? = null
        }
    }
}
