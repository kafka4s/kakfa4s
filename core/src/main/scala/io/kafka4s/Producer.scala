package io.kafka4s

import cats.{ApplicativeError, Monad}
import cats.data.Kleisli
import cats.implicits._
import io.kafka4s.serdes.Serializer

import scala.reflect.runtime.universe.{TypeTag, typeOf}

trait Producer[F[_]] {
  def send1: Kleisli[F, ProducerRecord[F], ProducerRecord[F]]

  def keyTypeHeaderName: String = "X-Key-ClassName"
  def valueTypeHeaderName: String = "X-Value-ClassName"

  def send[V](message: (String, V))(implicit F: Monad[F] with ApplicativeError[F, Throwable],
                                    SV: Serializer[V],
                                    TV: TypeTag[V]): F[ProducerRecord[F]] = {
    val (topic, value) = message
    send(topic, value)
  }

  def send[V](topic: String, value: V)(implicit F: Monad[F] with ApplicativeError[F, Throwable],
                                       SV: Serializer[V],
                                       TV: TypeTag[V]): F[ProducerRecord[F]] =
    for {
      vb <- F.fromEither(SV.serialize(value))
      tv <- Header.of[F](valueTypeHeaderName, typeOf[V].typeSymbol.fullName)
      p = ProducerRecord[F](topic, value = vb).add(tv)
      r <- send1(p)
    } yield r

  def send[K, V](topic: String, key: K, value: V)(implicit F: Monad[F] with ApplicativeError[F, Throwable],
                                                  SK: Serializer[K],
                                                  SV: Serializer[V],
                                                  TK: TypeTag[K],
                                                  TV: TypeTag[V]): F[ProducerRecord[F]] =
    for {
      kb <- F.fromEither(SK.serialize(key))
      vb <- F.fromEither(SV.serialize(value))
      tk <- Header.of[F](keyTypeHeaderName, typeOf[K].typeSymbol.fullName)
      tv <- Header.of[F](valueTypeHeaderName, typeOf[V].typeSymbol.fullName)
      p = ProducerRecord[F](topic, key = kb, value = vb).add(tv, tk)
      r <- send1(p)
    } yield r

  def send[K, V](topic: String, partition: Int, key: K, value: V)(implicit F: Monad[F] with ApplicativeError[F, Throwable],
                                                                  SK: Serializer[K],
                                                                  SV: Serializer[V],
                                                                  TK: TypeTag[K],
                                                                  TV: TypeTag[V]): F[ProducerRecord[F]] =
    for {
      kb <- F.fromEither(SK.serialize(key))
      vb <- F.fromEither(SV.serialize(value))
      tk <- Header.of[F](keyTypeHeaderName, typeOf[K].typeSymbol.fullName)
      tv <- Header.of[F](valueTypeHeaderName, typeOf[V].typeSymbol.fullName)
      p = ProducerRecord[F](topic, partition = Some(partition), key = kb, value = vb).add(tv, tk)
      r <- send1(p)
    } yield r
}