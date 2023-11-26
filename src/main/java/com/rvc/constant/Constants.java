package com.rvc.constant;

/**
 * @NAME: Constants
 * @USER: yuech
 * @Description:
 * @DATE: 2023/11/23
 */
public class Constants {

    public static final String EXCHANGE_TOPICS_INFORM="exchange_topics_inform";



    public static final String QUEUE_INFORM_TEXT = "queue_inform_text";

    public static final String QUEUE_INFORM_AUDIO = "queue_inform_audio";

    public static final String QUEUE_INFORM_IMAGE = "queue_inform_image";


    public static final String ROUTINGKEY_TEXT="inform.#.text.#";

    public static final String ROUTINGKEY_AUDIO="inform.#.audio.#";

    public static final String ROUTINGKEY_IMAGE="inform.#.image.#";
}