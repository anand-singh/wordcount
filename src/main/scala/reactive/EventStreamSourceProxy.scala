package reactive

/**
 * An EventStream that is implemented by delegating everything to an EventSource
 */
trait EventStreamSourceProxy[T] extends EventStream[T] with Observing {
  lazy val source: EventSource[T] = new EventSource[T]
  def flatMap[U](f: T=>EventStream[U]): EventStream[U] = source.flatMap[U](f)
  def foreach(f: T=>Unit)(implicit observing: Observing): Unit = source.foreach(f)(observing)
  def |[U>:T](that: EventStream[U]): EventStream[U] = source.|(that)
  def map[U](f: T=>U): EventStream[U] = source.map(f)
  def filter(f: T=>Boolean): EventStream[T] = source.filter(f)
  def collect[U](pf: PartialFunction[T,U]): EventStream[U] = source.collect(pf)
  def takeWhile(p: T=>Boolean): EventStream[T] = source.takeWhile(p)
  def foldLeft[U](initial: U)(f: (U,T)=>U): EventStream[U] = source.foldLeft(initial)(f)
  def hold[U>:T](init: U): Signal[U] = source.hold(init)
  def nonrecursive: EventStream[T] = source.nonrecursive

  private[reactive] def addListener(f: (T) => Unit): Unit = source.addListener(f)
  private[reactive] def removeListener(f: (T) => Unit): Unit = source.removeListener(f)
}

