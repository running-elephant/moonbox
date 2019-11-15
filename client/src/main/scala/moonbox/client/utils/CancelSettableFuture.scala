package moonbox.client.utils


import com.google.common.util.concurrent.AbstractFuture
import com.sun.istack.internal.Nullable


class CancelSettableFuture[V] extends AbstractFuture[V] {
	override def set(@Nullable value: V): Boolean = {
		super.set(value)
	}

	override def setException(throwable: Throwable): Boolean = {
		super.setException(throwable)
	}

}
