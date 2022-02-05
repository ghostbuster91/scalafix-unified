/** Copied from https://github.com/liancheng/scalafix-organize-imports/blob/master/project/TargetAxis.scala
  *
  * MIT License
  *
  * Copyright (c) 2020 Cheng Lian
  *
  * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
  * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
  * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
  * permit persons to whom the Software is furnished to do so, subject to the following conditions:
  *
  * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
  * Software.
  *
  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
  * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
  * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
  * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
  */

import sbt._
import sbt.internal.ProjectMatrix
import sbtprojectmatrix.ProjectMatrixPlugin.autoImport._

/** Use on ProjectMatrix rows to tag an affinity to a custom scalaVersion */
case class TargetAxis(scalaVersion: String) extends VirtualAxis.WeakAxis {

  private val scalaBinaryVersion = CrossVersion.binaryScalaVersion(scalaVersion)

  override val idSuffix = s"Target${scalaBinaryVersion.replace('.', '_')}"
  override val directorySuffix = s"target$scalaBinaryVersion"
}

object TargetAxis {

  private def targetScalaVersion(virtualAxes: Seq[VirtualAxis]): String =
    virtualAxes.collectFirst { case a: TargetAxis => a.scalaVersion }.get

  /** When invoked on a ProjectMatrix with a TargetAxis, lookup the project generated by `matrix` with a scalaVersion
    * matching the one declared in that TargetAxis, and resolve `key`.
    */
  def resolve[T](
      matrix: ProjectMatrix,
      key: TaskKey[T]
  ): Def.Initialize[Task[T]] =
    Def.taskDyn {
      val sv = targetScalaVersion(virtualAxes.value)
      val project = matrix.finder().apply(sv)
      Def.task((project / key).value)
    }

  /** When invoked on a ProjectMatrix with a TargetAxis, lookup the project generated by `matrix` with a scalaVersion
    * matching the one declared in that TargetAxis, and resolve `key`.
    */
  def resolve[T](
      matrix: ProjectMatrix,
      key: SettingKey[T]
  ): Def.Initialize[T] =
    Def.settingDyn {
      val sv = targetScalaVersion(virtualAxes.value)
      val project = matrix.finder().apply(sv)
      Def.setting((project / key).value)
    }

}