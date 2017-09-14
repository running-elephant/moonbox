/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2017 EDP
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

package edp.moonbox.grid.rest.serializers

import java.lang.reflect.InvocationTargetException

import akka.http.scaladsl.marshalling._
import akka.http.scaladsl.model.MediaTypes._
import akka.http.scaladsl.unmarshalling._
import akka.util.ByteString
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import de.heikoseeberger.akkahttpjson4s.Json4sSupport.ShouldWritePretty
import org.json4s.{Formats, MappingException, Serialization}


trait JsonSupport extends Json4sSupport {

    private val jsonStringUnmarshaller: FromEntityUnmarshaller[String] =
        Unmarshaller.byteStringUnmarshaller
          .forContentTypes(`application/json`, `text/plain`, `application/x-www-form-urlencoded`)
          .mapWithCharset {
              case (ByteString.empty, _) => throw Unmarshaller.NoContentException
              case (data, charset)       => data.decodeString(charset.nioCharset.name)
          }

    private val jsonStringMarshaller: ToEntityMarshaller[String] =
        Marshaller.stringMarshaller(`application/json`)

    /**
      * HTTP entity => `A`
      *
      * @tparam A type to decode
      * @return unmarshaller for `A`
      */

    override implicit def json4sUnmarshaller[A: Manifest](
                                                  implicit serialization: Serialization,
                                                  formats: Formats
                                                ): FromEntityUnmarshaller[A] =
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

    /**
      * `A` => HTTP entity
      *
      * @tparam A type to encode, must be upper bounded by `AnyRef`
      * @return marshaller for any `A` value
      */
    override implicit def json4sMarshaller[A <: AnyRef](
                                                implicit serialization: Serialization,
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
