<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Product Lookup</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 2rem; color: #1f2933; }
        h1 { margin-bottom: 0.5rem; }
        p.lead { margin-top: 0; color: #4b5563; }
        a.nav-link { color: #2563eb; text-decoration: none; margin-right: 1rem; }
        form { margin-top: 1.5rem; padding: 1rem; border: 1px solid #d1d5db; border-radius: 8px; max-width: 620px; }
        label { display: block; margin-top: 0.75rem; font-weight: bold; }
        input { width: 100%; padding: 0.5rem; margin-top: 0.25rem; border: 1px solid #c7ced6; border-radius: 4px; }
        button { margin-top: 1rem; padding: 0.5rem 1rem; border: none; border-radius: 4px; background: #2563eb; color: #fff; cursor: pointer; }
        .status { margin-top: 1rem; padding: 0.75rem; border-radius: 6px; }
        .status.success { background: #ecfdf5; border: 1px solid #a7f3d0; color: #065f46; }
        .status.error { background: #fef2f2; border: 1px solid #fecaca; color: #991b1b; }
        .status.info { background: #eff6ff; border: 1px solid #bfdbfe; color: #1d4ed8; }
        pre { background: #f3f4f6; padding: 1rem; border-radius: 6px; overflow-x: auto; max-width: 760px; }
    </style>
</head>
<body>
<nav>
    <a class="nav-link" href="/">Back to configuration</a>
    <a class="nav-link" href="/organizations">Organization lookup</a>
    <span>Targeting <code>${configuredBaseUrl}</code></span>
</nav>

<h1>Product Lookup</h1>
<p class="lead">Use the generated Hanaeco client to fetch products via <code>/organization/products</code>.</p>

<c:if test="${!configurationReady}">
    <div class="status info">
        Configure the Hanaeco base URL and API key on the <a href="/">main page</a> first.
    </div>
</c:if>

<form method="post" action="/products/query">
    <p>Enter the organization UID, then optionally filter the results by product ID or name.</p>

    <label for="organizationUid">Organization UID</label>
    <input type="text"
           id="organizationUid"
           name="organizationUid"
           value="${productOrganizationUidFieldValue}"
           placeholder="ex: org_xid_12345"
           required/>

    <label for="productId">Product UID</label>
    <input type="text"
           id="productId"
           name="productId"
           value="${productIdFieldValue}"
           placeholder="optional exact match"/>

    <label for="productName">Product Name</label>
    <input type="text"
           id="productName"
           name="productName"
           value="${productNameFieldValue}"
           placeholder="optional exact match"/>

    <button type="submit">Query Products</button>

    <c:if test="${productLookupMessage ne null}">
        <div class="status success">${productLookupMessage}</div>
    </c:if>
    <c:if test="${productLookupError ne null}">
        <div class="status error">${productLookupError}</div>
    </c:if>
</form>

<c:if test="${productLookupResult ne null}">
    <pre>${productLookupResult}</pre>
</c:if>
</body>
</html>
