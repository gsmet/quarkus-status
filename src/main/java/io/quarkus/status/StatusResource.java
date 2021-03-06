package io.quarkus.status;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.quarkus.qute.TemplateExtension;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.qute.api.CheckedTemplate;
import io.quarkus.status.model.Stats;
import io.quarkus.status.model.Status;
import org.ocpsoft.prettytime.PrettyTime;

@Path("/")
public class StatusResource {

    @Inject
    StatusService statusService;

    @Inject
    IssuesService issuesService;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance index(Status status);
        public static native TemplateInstance issues(Stats stats);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance index() throws IOException {
        return Templates.index(statusService.getStatus());
    }

    @GET
    @Path("bugs")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance bugs() throws IOException {
        return Templates.issues(issuesService.getBugsMonthlyStats());
    }

    @GET
    @Path("enhancements")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance features() throws IOException {
        return Templates.issues(issuesService.getEnhancementsMonthlyStats());
    }

    @TemplateExtension
    static class Extensions {

        static String formatDateTime(LocalDateTime dateTime) {
            PrettyTime p = new PrettyTime();
            return p.format(Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant()));
        }
    }
}