/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2019 EDP
 * ==
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * >>
 */

package moonbox.grid.deploy.rest

import java.lang.reflect.InvocationTargetException
import java.math.BigInteger
import java.sql.Timestamp

import akka.http.scaladsl.marshalling.{Marshaller, _}
import akka.http.scaladsl.model.MediaTypes._
import akka.http.scaladsl.unmarshalling.{Unmarshaller, _}
import akka.util.ByteString
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import de.heikoseeberger.akkahttpjson4s.Json4sSupport.ShouldWritePretty
import moonbox.common.util.Utils
import org.json4s.JsonAST.JInt
import org.json4s.jackson.Serialization
import org.json4s.{CustomSerializer, DefaultFormats, Formats, JString, MappingException, Serialization, Serializer}

trait JsonSerializer extends Json4sSupport {
  implicit val formats = DefaultFormats ++ customFormats
  implicit val serialization = Serialization
  implicit val shouldWritePretty = ShouldWritePretty.True

  def customFormats: Traversable[Serializer[_]] = {
    Seq(
      new CustomSerializer[java.sql.Date](_ => ( {
        case JInt(s) => new java.sql.Date(s.longValue())
      }, {
        case x: java.sql.Date => JString(Utils.formatDate(x))
      }
      )
      ),
      new CustomSerializer[java.sql.Timestamp](_ => ( {
        case JInt(s) => new Timestamp(s.longValue())
      }, {
        case x: java.sql.Timestamp => JString(Utils.formatTimestamp(x))
      }
      )
      ),
      new CustomSerializer[java.math.BigDecimal](_ => ( {
        case JString(s) => new java.math.BigDecimal(s)
      }, {
        case b: java.math.BigDecimal => JString(b.toString)
      }
      )
      ),
      new CustomSerializer[java.math.BigInteger](_ => ( {
        case JString(s) => new BigInteger(s)
      }, {
        case b: java.math.BigInteger => JString(b.toString)
      }
      )
      )
    )
  }

  val jsonStringUnmarshaller: FromEntityUnmarshaller[String] =
    Unmarshaller.byteStringUnmarshaller
      .forContentTypes(`application/json`, `text/plain`, `application/x-www-form-urlencoded`)
      .mapWithCharset {
        case (ByteString.empty, _) => throw Unmarshaller.NoContentException
        case (data, charset) => data.decodeString(charset.nioCharset.name)
      }

  val jsonStringMarshaller: ToEntityMarshaller[String] =
    Marshaller.stringMarshaller(`application/json`)


  // HTTP entity => `A`
  override implicit def json4sUnmarshaller[A: Manifest](implicit serialization: Serialization,
                                                        formats: Formats): FromEntityUnmarshaller[A] =
    jsonStringUnmarshaller.map { data =>
      serialization.read(data)
    }.recover(
      _ =>
        _ => {
          case MappingException("unknown error",
          ite: InvocationTargetException) =>
            throw ite.getCause
        }
    )


  // `A` => HTTP entity
  override implicit def json4sMarshaller[A <: AnyRef](implicit serialization: Serialization,
                                                      formats: Formats,
                                                      shouldWritePretty: ShouldWritePretty = ShouldWritePretty.False
                                                     ): ToEntityMarshaller[A] =
    shouldWritePretty match {
      case ShouldWritePretty.False =>
        jsonStringMarshaller.compose(serialization.write[A])
      case ShouldWritePretty.True =>
        jsonStringMarshaller.compose(serialization.writePretty[A])
    }
}
