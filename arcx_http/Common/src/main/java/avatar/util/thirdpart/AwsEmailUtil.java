package avatar.util.thirdpart;

import avatar.global.basicConfig.basic.AwsEmailConfigMsg;
import avatar.util.LogUtil;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

/**

 */
public class AwsEmailUtil {

    /**

     */
    public static void sendEmail(String userEmail){
        String configSet = "ConfigSet";
        
        String subject = "Amazon SES test (AWS SDK for Java)";
        
        String htmlBody = "<h1>Amazon SES test (AWS SDK for Java)</h1>"
                + "<p>This email was sent with <a href='https://aws.amazon.com/ses/'>"
                + "Amazon SES</a> using the <a href='https://aws.amazon.com/sdk-for-java/'>"
                + "AWS SDK for Java</a>";
        
        String textBody = "This email was sent through Amazon SES "
                + "using the AWS SDK for Java.";
        AmazonSimpleEmailService client =
                AmazonSimpleEmailServiceClientBuilder.standard()
                        // Replace US_WEST_2 with the AWS Region you're using for
                        // Amazon SES.
                        .withRegion(Regions.AP_SOUTHEAST_1).build();
        SendEmailRequest request = new SendEmailRequest()
                .withDestination(
                        new Destination().withToAddresses(userEmail))
                .withMessage(new Message()
                        .withBody(new Body()
                                .withHtml(new Content()
                                        .withCharset("UTF-8").withData(htmlBody))
                                .withText(new Content()
                                        .withCharset("UTF-8").withData(textBody)))
                        .withSubject(new Content()
                                .withCharset("UTF-8").withData(subject)))
                .withSource(AwsEmailConfigMsg.arcxEmail);
//                .withConfigurationSetName(configSet);
        client.sendEmail(request);

    }
}
