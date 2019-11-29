package moonbox.mbw.service.impl;

import moonbox.catalog.CatalogApplication;
import moonbox.catalog.JdbcCatalog;
import moonbox.mbw.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@SuppressWarnings("unchecked")
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    private JdbcCatalog catalog;

    @Override
    public List<CatalogApplication> listApps() {
        return (List<CatalogApplication>) catalog.listApplications();
    }
}
