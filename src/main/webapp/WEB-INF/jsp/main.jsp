<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>HanaEco Integration Test Tool</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 2rem; color: #1f2933; }
        h1 { margin-bottom: 0.5rem; }
        p.lead { margin-top: 0; color: #4b5563; }
        form { margin-top: 1.5rem; padding: 1rem; border: 1px solid #d1d5db; border-radius: 8px; max-width: 480px; }
        label { display: block; margin-top: 0.75rem; font-weight: bold; }
        input { width: 100%; padding: 0.5rem; margin-top: 0.25rem; border: 1px solid #c7ced6; border-radius: 4px; }
        button { margin-top: 1rem; padding: 0.5rem 1rem; border: none; border-radius: 4px; background: #2563eb; color: #fff; cursor: pointer; }
        button.secondary { background: #10b981; }
        a.button-link { display: inline-block; margin-top: 1rem; padding: 0.5rem 1rem; border-radius: 4px; background: #f97316; color: #fff; text-decoration: none; }
        .status { margin-top: 1rem; padding: 0.75rem; border-radius: 6px; }
        .status.success { background: #ecfdf5; border: 1px solid #a7f3d0; color: #065f46; }
        .status.error { background: #fef2f2; border: 1px solid #fecaca; color: #991b1b; }
        pre { background: #f3f4f6; padding: 1rem; border-radius: 6px; overflow-x: auto; }
    </style>
</head>
<body>
<h1>HanaEco Integration Test Tool</h1>
<p class="lead">This Java-based application integrates with the Hanaeco server through its API.</p>
<a class="button-link" href="/organizations">Open Organization Lookup Page</a>
<a class="button-link" href="/products" style="background:#0ea5e9;">Open Product Lookup Page</a>

<form method="post" action="/configure">
    <h2>Configure Hanaeco Client</h2>
    <label for="baseUrl">Hanaeco Base URL</label>
    <input type="url" id="baseUrl" name="baseUrl" value="${baseUrlFieldValue}" required/>

    <label for="apiKey">API Key</label>
    <input type="password" id="apiKey" name="apiKey" value="${apiKeyFieldValue}" required/>

    <button type="submit">Save Configuration</button>

    <c:if test="${configurationMessage ne null}">
        <div class="status success">${configurationMessage}</div>
    </c:if>
    <c:if test="${configurationError ne null}">
        <div class="status error">${configurationError}</div>
    </c:if>
</form>

<c:if test="${configurationReady}">
    <form method="post" action="/fetch-info">
        <h2>Fetch Hanaeco Info</h2>
        <p>Use the configured values to call <code>${configuredBaseUrl}/info</code>.</p>
        <button type="submit" class="secondary">Fetch Info</button>
    </form>
</c:if>

<c:if test="${fetchResult ne null}">
    <div class="status success">
        Configuration is set. Latest info payload from Hanaeco:
    </div>
    <pre>${fetchResult}</pre>
</c:if>

<c:if test="${fetchError ne null}">
    <div class="status error">${fetchError}</div>
</c:if>
</body>
</html>
