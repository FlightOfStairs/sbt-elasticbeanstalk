package com.blendlabsinc.sbtelasticbeanstalk

import com.blendlabsinc.sbtelasticbeanstalk.ElasticBeanstalkKeys._
import sbt.Setting
import sbt.Keys.commands

trait ElasticBeanstalkSettings {
  this: ElasticBeanstalkCommands with ElasticBeanstalkAPICommands =>

  lazy val elasticBeanstalkSettings = Seq[Setting[_]](
    ebDeploy <<= ebDeployTask,
    ebWait <<= ebWaitForEnvironmentsTask,
    ebApiDescribeApplications <<= ebApiDescribeApplicationsTask,
    ebApiDescribeEnvironments <<= ebApiDescribeEnvironmentsTask,
    commands ++= Seq(ebApiRestartAppServer),
    ebRequireJava6 := true
  )
}
