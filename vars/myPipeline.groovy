def call(Map pipelineParams){
pipeline {
   agent any

   tools {
      // Install the Maven version configured as "M3" and add it to the path.
      maven "Maven"
      
   }

   stages {
       stage('checkout'){
           steps{
               // Get some code from a GitHub repository
               
            //   git 'https://github.com/SrikarChiku/Java-my-app.git'
            git branch:"master",url:'https://github.com/SrikarChiku/Java-my-app.git'
           }
       }
      stage('Compile') {
         steps {
            // Run Maven on a Unix agent.
            bat "mvn clean compile"
         }
            post{
                success {
                    echo "compile successful"
                }
            }
      }
      stage ('install'){
          steps{
	  echo "${pipelineParams.buildNumber}"
            //   bat "mvn -Dmaven.main.skip=true integration-test"
            bat "mvn install -DbuildNumber=${pipelineParams.buildNumber}"
              
          }
          post {
              success{
                        echo "install and test successful"
              }

          }
      }
      stage ('deploy'){
          steps{
               //bat "mvn -s settings.xml -Dmaven.test.skip=true deploy"
               bat "jfrog rt u target/jfrog-app-SNAPSHOT-*.*.*.*.jar libs-snapshot-local/com/jfrog/app/jfrog-app/"
          }
          post{
              success{
                    mail bcc: '', body: '''hey Srikar,
                    Deploy Successful to the jfrog artifactory''', cc: '',
                    from: '', replyTo: '', subject: 'Deploy Successful',
                    to: 'iamsri15@gmail.com'
          }
          }
      }
   }
}
}
