package core

import spray.json._

sealed trait TwitterModel
object TwitterModel extends DefaultJsonProtocol {
  implicit val jsonHashTag = jsonFormat2(HashTag.apply)
  implicit val jsonUserMention = jsonFormat5(UserMention.apply)
  implicit val jsonTweetURL = jsonFormat4(TweetUrl.apply)
  implicit val jsonSize = jsonFormat3(Size.apply)
  implicit val jsonMedia = jsonFormat10(Media.apply)
  implicit val jsonEntities = jsonFormat4(Entities.apply)
  implicit val jsonUser = jsonFormat16(User.apply)
  implicit val jsonTweet = jsonFormat16(Tweet.apply)
}

case class TweetUrl(url: String,
                    display_url: String,
                    expanded_url: String,
                    indices: List[Int])
    extends TwitterModel

case class Entities(urls: List[TweetUrl],
                    user_mentions: List[UserMention],
                    hashtags: List[HashTag],
                    media: Option[List[Media]])
    extends TwitterModel

case class Media(id: Int,
                 id_str: String,
                 media_url: String,
                 media_url_https: String,
                 url: String,
                 display_url: String,
                 expanded_url: String,
                 sizes: Map[String, Size],
                 `type`: String,
                 indices: List[Int])
    extends TwitterModel

case class Size(w: Int,
                h: Int,
                resize: String)


    extends TwitterModel

case class UserMention(id: Int,
                       id_str: String,
                       screen_name: String,
                       name: String,
                       indices: List[Int])
    extends TwitterModel

case class HashTag(text: String,
                   indices: List[Int])


case class TimeLine(tweets: List[Tweet])
    extends TwitterModel

case class Tweet(id: BigInt,
                 id_str: String,
                 created_at: String,
                 favorited: Boolean,
                 in_reply_to_screen_name: Option[String],
                 in_reply_to_user_id: Option[BigInt],
                 in_reply_to_user_id_str: Option[String],
                 in_reply_to_status_id: Option[BigInt],
                 in_reply_to_status_id_str: Option[String],
                 text: String,
                 retweet_count: Int,
                 truncated: Boolean,
                 retweeted: Boolean,
                 source: String,
                 entities: Option[Entities],
                 user: User)
    extends TwitterModel

case class User(id: BigInt,
                id_str: String,
                url: Option[String],
                created_at: String,
                utc_offset: Option[Int],
                screen_name: String,
                name: String,
                time_zone: Option[String],
                location: Option[String],
                lang: String,
                description: Option[String],

//                profile_use_background_image: Boolean,
//                profile_background_image_url_https: String,
//                profile_text_color: String,
//                profile_sidebar_border_color: String,
//                profile_image_url: String,
//                profile_background_tile: Boolean,
//                default_profile_image: Boolean,
//                profile_sidebar_fill_color: String,
//                profile_background_color: String,
//                profile_image_url_https: String,
//                profile_background_image_url: String,
//                default_profile: Boolean,
//                profile_link_color: String,

                listed_count: Int,
                friends_count: Int,
                statuses_count: Int,
                followers_count: Int,
                favourites_count: Int

//                `protected`: Boolean,
//                geo_enabled: Boolean,
//                contributors_enabled: Boolean,
//                is_translator: Boolean,
//                show_all_inline_media: Option[Boolean],
//                notifications: Boolean,
//                follow_request_sent: Boolean,
//                following: Boolean,
//                verified: Boolean
                 )
    extends TwitterModel
