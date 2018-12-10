/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2018 EDP
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

package moonbox.catalog

import java.security.MessageDigest

object PasswordEncryptor {
	private val KEY_SHA = "SHA"
	private val hexDigits = (0 to 9).++('a' to 'f').map(_.toString)

	def encryptSHA(data: String): String = {
		if (data == null || data.equals("")) {
			 ""
		} else {
			val sha = MessageDigest.getInstance(KEY_SHA)
			sha.update(data.getBytes)
			byteArrayToHexString(sha.digest()).toUpperCase
		}
	}

	private def byteArrayToHexString(bytes: Array[Byte]): String = {
		bytes.map(byteToHexString).reduce(_ + _)
	}

	private def byteToHexString(byte: Byte): String = {
		val res = if (byte < 0) byte + 256
		else byte
		hexDigits(res / 16) + hexDigits(res % 16)
	}
}
