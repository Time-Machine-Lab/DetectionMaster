package com.rvc.constant;

/**
 * @NAME: DetectionConstant
 * @USER: yuech
 * @Description:
 * @DATE: 2023/11/23
 */
public class DetectionConstant {
    /**
     * 消息队列
     */

    public static final String RES_EXCHANGE_NAME = "res.topic";

    public static final String DETECTION_EXCHANGE_NAME =  "detection.topic";



    public static final String ROUTER_KEY_HEADER = "res.";

    public static final String TEXT_QUEUE_NAME = "detection.text";
    public static final String TEXT_ROUTER_KEY = "detection.text";

    public static final String IMAGE_QUEUE_NAME = "detection.image";
    public static final String IMAGE_ROUTER_KEY = "detection.image";

    public static final String AUDIO_QUEUE_NAME = "detection.audio";
    public static final String AUDIO_ROUTER_KEY = "detection.audio";


    /**
     * 违规说明
     */

    public static final String NONLABEL = "nonLabel";

//    public static final String EXCHANGE_TOPICS_INFORM="exchange_topics_inform";
//
//    public static final String EXCHANGE_TOPICS_STATUS="exchange_topics_status";
//
//
//    public static final String QUEUE_INFORM_TEXT = "queue_inform_text";
//
//    public static final String QUEUE_INFORM_AUDIO = "queue_inform_audio";
//
//    public static final String QUEUE_INFORM_IMAGE = "queue_inform_image";
//
//
//    public static final String ROUTINGKEY_TEXT="informtext";
//
//    public static final String ROUTINGKEY_AUDIO="informaudio";
//
//    public static final String ROUTINGKEY_IMAGE="informimage";


    /**
     * 文本服务
     */
//    用户昵称检测
    public static final String NICKNAME_DETECTION = "nickname_detection";
//    私聊互动内容检测
    public static final String CHAT_DETECTION = "chat_detection";
//    公聊评论内容检测
    public static final String COMMENT_DETECTION = "comment_detection";
//    PGC教学物料检测
    public static final String PGC_DETECTION = "pgc_detection";
//    AIGC类文字检测
    public static final String AI_ART_DETECTION = "ai_art_detection";
//    广告法合规检测
    public static final String AD_COMPLIANCE_DETECTION = "ad_compliance_detection";
//    国际业务多语言检测
    public static final String COMMENT_MULTILINGUAL_PRO = "comment_multilingual_pro";
//    URL风险链接检测
    public static final String URL_DETECTION = "url_detection";

    /**
     * 图片服务
     */
//    通用基线检测
    public static final String BASELINECHECK = "baselineCheck";
//    通用基线检测_专业版
    public static final String BASELINECHECK_PRO = "baselineCheck_pro";
//    通用基线检测_海外版
    public static final String BASELINECHECK_CB = "baselineCheck_cb";
//    内容治理检测
    public static final String TONALITYIMPROVE = "tonalityImprove";
//    AIGC图片检测
    public static final String AIGCCHECK = "aigcCheck";
//    头像图片检测
    public static final String PROFILEPHOTOCHECK = "profilePhotoCheck";
//    营销素材检测
    public static final String ADVERTISINGCHECK = "advertisingCheck";
//    视频/直播截图检测
    public static final String LIVESTREAMCHECK = "liveStreamCheck";

    /**
     * 语音服务
     */
//    社交娱乐直播检测
    public static final String LIVE_STREAM_DETECTION = "live_stream_detection";
//    音视频媒体检测
    public static final String AUDIO_MEDIA_DETECTION = "audio_media_detection";
//    音视频媒体多语言检测
    public static final String AUDIO_MULTILINGUAL_CB = "audio_multilingual_cb";


    /**
     * 审核状态
     */
    public static final Integer DETECTION_SUCCESS = 1;
    public static final Integer DETECTION_FAIL = 2;

}