package com.blendlabsinc.sbtelasticbeanstalk

import com.blendlabsinc.sbtelasticbeanstalk.{ ElasticBeanstalkKeys => eb }
import com.blendlabsinc.sbtelasticbeanstalk.core.{ AWS, Deployer, SourceBundleUploader }
import com.github.play2war.plugin.Play2WarKeys
import sbt.Keys.{ version, streams }

trait ElasticBeanstalkCommands {
  val ebDeployTask = (Play2WarKeys.war, eb.ebS3BucketName, eb.ebDeployments, eb.ebRegion, streams) map {
    (war, s3BucketName, ebDeployments, ebRegion, s) => {
      s.log.info("Uploading " + war.getName + " (" + (war.length/1024/1024) + " MB) " +
                 "to Amazon S3 bucket '" + s3BucketName + "'")
      val u = new SourceBundleUploader(war, s3BucketName, AWS.awsCredentials)
      val bundleLocation = u.upload()

      s.log.info("WAR file upload complete.")

      val versionLabel = bundleLocation.getS3Key

      for (deployment <- ebDeployments) {
        s.log.info(
          "Deploying to Elastic Beanstalk:\n" + 
          "  WAR file: " + war.getName + "\n" +
          "  EB app version label: " + versionLabel + "\n" +
          "  EB app: " + deployment.appName + "\n" +
          "  EB environment: " + deployment.environmentName + "\n" +
          "  Region: " + ebRegion + "\n\n"
        )
        val d = new Deployer(
          deployment.appName,
          deployment.environmentName,
          versionLabel,
          bundleLocation,
          AWS.awsCredentials,
          ebRegion
        )
        val res = d.deploy()

        s.log.info("Elastic Beanstalk deployment complete.\n" +
                   "URL: http://" + res.getCNAME() + "\n" +
                   "Status: " + res.getHealth())
      }
    }
  }
}
