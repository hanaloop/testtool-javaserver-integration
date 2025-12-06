<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Organization Lookup</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 2rem; color: #1f2933; }
        h1 { margin-bottom: 0.5rem; }
        p.lead { margin-top: 0; color: #4b5563; }
        a.nav-link { color: #2563eb; text-decoration: none; margin-right: 1rem; }
        form { margin-top: 1.5rem; padding: 1rem; border: 1px solid #d1d5db; border-radius: 8px; max-width: 560px; }
        label { display: block; margin-top: 0.75rem; font-weight: bold; }
        input { width: 100%; padding: 0.5rem; margin-top: 0.25rem; border: 1px solid #c7ced6; border-radius: 4px; }
        button { margin-top: 1rem; padding: 0.5rem 1rem; border: none; border-radius: 4px; background: #2563eb; color: #fff; cursor: pointer; }
        .status { margin-top: 1rem; padding: 0.75rem; border-radius: 6px; }
        .status.success { background: #ecfdf5; border: 1px solid #a7f3d0; color: #065f46; }
        .status.error { background: #fef2f2; border: 1px solid #fecaca; color: #991b1b; }
        .status.info { background: #eff6ff; border: 1px solid #bfdbfe; color: #1d4ed8; }
        pre { background: #f3f4f6; padding: 1rem; border-radius: 6px; overflow-x: auto; max-width: 720px; }
    </style>
</head>
<body>
<nav>
    <a class="nav-link" href="/">Back to configuration</a>
    <a class="nav-link" href="/products">Product lookup</a>
    <span>Targeting <code>${configuredBaseUrl}</code></span>
 </nav>

<h1>Organization Lookup</h1>
<p class="lead">Fetch organization details from Hanaeco by supplying either the organization ID (xid) or its name.</p>

<c:if test="${!configurationReady}">
    <div class="status info">
        Configure the Hanaeco base URL and API key on the <a href="/">main page</a> first.
    </div>
</c:if>

<form method="post" action="/organizations/query">
    <p>Provide either the organization ID or name. If both are provided, the ID lookup takes priority.</p>

    <label for="organizationId">Organization ID</label>
    <input type="text"
           id="organizationId"
           name="organizationId"
           value="${organizationIdFieldValue}"
           placeholder="ex: org_xid_12345"/>

    <label for="organizationName">Organization Name</label>
    <input type="text"
           id="organizationName"
           name="organizationName"
           value="${organizationNameFieldValue}"
           placeholder="ex: Hana Loop Manufacturing"/>

    <button type="submit">Query Organization</button>

    <c:if test="${organizationLookupMessage ne null}">
        <div class="status success">${organizationLookupMessage}</div>
    </c:if>
    <c:if test="${organizationLookupError ne null}">
        <div class="status error">${organizationLookupError}</div>
    </c:if>
</form>

<c:if test="${organizationLookupResult ne null}">
    <pre>${organizationLookupResult}</pre>
</c:if>
</body>
</html>
