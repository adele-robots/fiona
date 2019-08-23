// This is a generated source file for Chilkat version 9.5.0.43
#ifndef _C_CkImap_H
#define _C_CkImap_H
#include "chilkatDefs.h"

#include "Chilkat_C.h"

CK_VISIBLE_PUBLIC HCkImap CkImap_Create(void);
CK_VISIBLE_PUBLIC void CkImap_Dispose(HCkImap handle);
CK_VISIBLE_PUBLIC BOOL CkImap_getAppendSeen(HCkImap cHandle);
CK_VISIBLE_PUBLIC void CkImap_putAppendSeen(HCkImap cHandle, BOOL newVal);
CK_VISIBLE_PUBLIC int CkImap_getAppendUid(HCkImap cHandle);
CK_VISIBLE_PUBLIC void CkImap_getAuthMethod(HCkImap cHandle, HCkString retval);
CK_VISIBLE_PUBLIC void CkImap_putAuthMethod(HCkImap cHandle, const char *newVal);
CK_VISIBLE_PUBLIC const char *CkImap_authMethod(HCkImap cHandle);
CK_VISIBLE_PUBLIC void CkImap_getAuthzId(HCkImap cHandle, HCkString retval);
CK_VISIBLE_PUBLIC void CkImap_putAuthzId(HCkImap cHandle, const char *newVal);
CK_VISIBLE_PUBLIC const char *CkImap_authzId(HCkImap cHandle);
CK_VISIBLE_PUBLIC BOOL CkImap_getAutoDownloadAttachments(HCkImap cHandle);
CK_VISIBLE_PUBLIC void CkImap_putAutoDownloadAttachments(HCkImap cHandle, BOOL newVal);
CK_VISIBLE_PUBLIC BOOL CkImap_getAutoFix(HCkImap cHandle);
CK_VISIBLE_PUBLIC void CkImap_putAutoFix(HCkImap cHandle, BOOL newVal);
CK_VISIBLE_PUBLIC void CkImap_getClientIpAddress(HCkImap cHandle, HCkString retval);
CK_VISIBLE_PUBLIC void CkImap_putClientIpAddress(HCkImap cHandle, const char *newVal);
CK_VISIBLE_PUBLIC const char *CkImap_clientIpAddress(HCkImap cHandle);
CK_VISIBLE_PUBLIC int CkImap_getConnectTimeout(HCkImap cHandle);
CK_VISIBLE_PUBLIC void CkImap_putConnectTimeout(HCkImap cHandle, int newVal);
CK_VISIBLE_PUBLIC void CkImap_getConnectedToHost(HCkImap cHandle, HCkString retval);
CK_VISIBLE_PUBLIC const char *CkImap_connectedToHost(HCkImap cHandle);
CK_VISIBLE_PUBLIC void CkImap_getDebugLogFilePath(HCkImap cHandle, HCkString retval);
CK_VISIBLE_PUBLIC void CkImap_putDebugLogFilePath(HCkImap cHandle, const char *newVal);
CK_VISIBLE_PUBLIC const char *CkImap_debugLogFilePath(HCkImap cHandle);
CK_VISIBLE_PUBLIC void CkImap_getDomain(HCkImap cHandle, HCkString retval);
CK_VISIBLE_PUBLIC void CkImap_putDomain(HCkImap cHandle, const char *newVal);
CK_VISIBLE_PUBLIC const char *CkImap_domain(HCkImap cHandle);
CK_VISIBLE_PUBLIC int CkImap_getHeartbeatMs(HCkImap cHandle);
CK_VISIBLE_PUBLIC void CkImap_putHeartbeatMs(HCkImap cHandle, int newVal);
CK_VISIBLE_PUBLIC void CkImap_getHttpProxyAuthMethod(HCkImap cHandle, HCkString retval);
CK_VISIBLE_PUBLIC void CkImap_putHttpProxyAuthMethod(HCkImap cHandle, const char *newVal);
CK_VISIBLE_PUBLIC const char *CkImap_httpProxyAuthMethod(HCkImap cHandle);
CK_VISIBLE_PUBLIC void CkImap_getHttpProxyDomain(HCkImap cHandle, HCkString retval);
CK_VISIBLE_PUBLIC void CkImap_putHttpProxyDomain(HCkImap cHandle, const char *newVal);
CK_VISIBLE_PUBLIC const char *CkImap_httpProxyDomain(HCkImap cHandle);
CK_VISIBLE_PUBLIC void CkImap_getHttpProxyHostname(HCkImap cHandle, HCkString retval);
CK_VISIBLE_PUBLIC void CkImap_putHttpProxyHostname(HCkImap cHandle, const char *newVal);
CK_VISIBLE_PUBLIC const char *CkImap_httpProxyHostname(HCkImap cHandle);
CK_VISIBLE_PUBLIC void CkImap_getHttpProxyPassword(HCkImap cHandle, HCkString retval);
CK_VISIBLE_PUBLIC void CkImap_putHttpProxyPassword(HCkImap cHandle, const char *newVal);
CK_VISIBLE_PUBLIC const char *CkImap_httpProxyPassword(HCkImap cHandle);
CK_VISIBLE_PUBLIC int CkImap_getHttpProxyPort(HCkImap cHandle);
CK_VISIBLE_PUBLIC void CkImap_putHttpProxyPort(HCkImap cHandle, int newVal);
CK_VISIBLE_PUBLIC void CkImap_getHttpProxyUsername(HCkImap cHandle, HCkString retval);
CK_VISIBLE_PUBLIC void CkImap_putHttpProxyUsername(HCkImap cHandle, const char *newVal);
CK_VISIBLE_PUBLIC const char *CkImap_httpProxyUsername(HCkImap cHandle);
CK_VISIBLE_PUBLIC BOOL CkImap_getKeepSessionLog(HCkImap cHandle);
CK_VISIBLE_PUBLIC void CkImap_putKeepSessionLog(HCkImap cHandle, BOOL newVal);
CK_VISIBLE_PUBLIC void CkImap_getLastAppendedMime(HCkImap cHandle, HCkString retval);
CK_VISIBLE_PUBLIC const char *CkImap_lastAppendedMime(HCkImap cHandle);
CK_VISIBLE_PUBLIC void CkImap_getLastCommand(HCkImap cHandle, HCkString retval);
CK_VISIBLE_PUBLIC const char *CkImap_lastCommand(HCkImap cHandle);
CK_VISIBLE_PUBLIC void CkImap_getLastErrorHtml(HCkImap cHandle, HCkString retval);
CK_VISIBLE_PUBLIC const char *CkImap_lastErrorHtml(HCkImap cHandle);
CK_VISIBLE_PUBLIC void CkImap_getLastErrorText(HCkImap cHandle, HCkString retval);
CK_VISIBLE_PUBLIC const char *CkImap_lastErrorText(HCkImap cHandle);
CK_VISIBLE_PUBLIC void CkImap_getLastErrorXml(HCkImap cHandle, HCkString retval);
CK_VISIBLE_PUBLIC const char *CkImap_lastErrorXml(HCkImap cHandle);
CK_VISIBLE_PUBLIC void CkImap_getLastIntermediateResponse(HCkImap cHandle, HCkString retval);
CK_VISIBLE_PUBLIC const char *CkImap_lastIntermediateResponse(HCkImap cHandle);
CK_VISIBLE_PUBLIC void CkImap_getLastResponse(HCkImap cHandle, HCkString retval);
CK_VISIBLE_PUBLIC const char *CkImap_lastResponse(HCkImap cHandle);
CK_VISIBLE_PUBLIC void CkImap_getLastResponseCode(HCkImap cHandle, HCkString retval);
CK_VISIBLE_PUBLIC const char *CkImap_lastResponseCode(HCkImap cHandle);
CK_VISIBLE_PUBLIC void CkImap_getLoggedInUser(HCkImap cHandle, HCkString retval);
CK_VISIBLE_PUBLIC const char *CkImap_loggedInUser(HCkImap cHandle);
CK_VISIBLE_PUBLIC int CkImap_getNumMessages(HCkImap cHandle);
CK_VISIBLE_PUBLIC BOOL CkImap_getPeekMode(HCkImap cHandle);
CK_VISIBLE_PUBLIC void CkImap_putPeekMode(HCkImap cHandle, BOOL newVal);
CK_VISIBLE_PUBLIC int CkImap_getPort(HCkImap cHandle);
CK_VISIBLE_PUBLIC void CkImap_putPort(HCkImap cHandle, int newVal);
CK_VISIBLE_PUBLIC BOOL CkImap_getPreferIpv6(HCkImap cHandle);
CK_VISIBLE_PUBLIC void CkImap_putPreferIpv6(HCkImap cHandle, BOOL newVal);
CK_VISIBLE_PUBLIC int CkImap_getReadTimeout(HCkImap cHandle);
CK_VISIBLE_PUBLIC void CkImap_putReadTimeout(HCkImap cHandle, int newVal);
CK_VISIBLE_PUBLIC BOOL CkImap_getRequireSslCertVerify(HCkImap cHandle);
CK_VISIBLE_PUBLIC void CkImap_putRequireSslCertVerify(HCkImap cHandle, BOOL newVal);
CK_VISIBLE_PUBLIC void CkImap_getSearchCharset(HCkImap cHandle, HCkString retval);
CK_VISIBLE_PUBLIC void CkImap_putSearchCharset(HCkImap cHandle, const char *newVal);
CK_VISIBLE_PUBLIC const char *CkImap_searchCharset(HCkImap cHandle);
CK_VISIBLE_PUBLIC void CkImap_getSelectedMailbox(HCkImap cHandle, HCkString retval);
CK_VISIBLE_PUBLIC const char *CkImap_selectedMailbox(HCkImap cHandle);
CK_VISIBLE_PUBLIC int CkImap_getSendBufferSize(HCkImap cHandle);
CK_VISIBLE_PUBLIC void CkImap_putSendBufferSize(HCkImap cHandle, int newVal);
CK_VISIBLE_PUBLIC char CkImap_getSeparatorChar(HCkImap cHandle);
CK_VISIBLE_PUBLIC void CkImap_putSeparatorChar(HCkImap cHandle, char newVal);
CK_VISIBLE_PUBLIC void CkImap_getSessionLog(HCkImap cHandle, HCkString retval);
CK_VISIBLE_PUBLIC const char *CkImap_sessionLog(HCkImap cHandle);
CK_VISIBLE_PUBLIC int CkImap_getSoRcvBuf(HCkImap cHandle);
CK_VISIBLE_PUBLIC void CkImap_putSoRcvBuf(HCkImap cHandle, int newVal);
CK_VISIBLE_PUBLIC int CkImap_getSoSndBuf(HCkImap cHandle);
CK_VISIBLE_PUBLIC void CkImap_putSoSndBuf(HCkImap cHandle, int newVal);
CK_VISIBLE_PUBLIC void CkImap_getSocksHostname(HCkImap cHandle, HCkString retval);
CK_VISIBLE_PUBLIC void CkImap_putSocksHostname(HCkImap cHandle, const char *newVal);
CK_VISIBLE_PUBLIC const char *CkImap_socksHostname(HCkImap cHandle);
CK_VISIBLE_PUBLIC void CkImap_getSocksPassword(HCkImap cHandle, HCkString retval);
CK_VISIBLE_PUBLIC void CkImap_putSocksPassword(HCkImap cHandle, const char *newVal);
CK_VISIBLE_PUBLIC const char *CkImap_socksPassword(HCkImap cHandle);
CK_VISIBLE_PUBLIC int CkImap_getSocksPort(HCkImap cHandle);
CK_VISIBLE_PUBLIC void CkImap_putSocksPort(HCkImap cHandle, int newVal);
CK_VISIBLE_PUBLIC void CkImap_getSocksUsername(HCkImap cHandle, HCkString retval);
CK_VISIBLE_PUBLIC void CkImap_putSocksUsername(HCkImap cHandle, const char *newVal);
CK_VISIBLE_PUBLIC const char *CkImap_socksUsername(HCkImap cHandle);
CK_VISIBLE_PUBLIC int CkImap_getSocksVersion(HCkImap cHandle);
CK_VISIBLE_PUBLIC void CkImap_putSocksVersion(HCkImap cHandle, int newVal);
CK_VISIBLE_PUBLIC BOOL CkImap_getSsl(HCkImap cHandle);
CK_VISIBLE_PUBLIC void CkImap_putSsl(HCkImap cHandle, BOOL newVal);
CK_VISIBLE_PUBLIC void CkImap_getSslProtocol(HCkImap cHandle, HCkString retval);
CK_VISIBLE_PUBLIC void CkImap_putSslProtocol(HCkImap cHandle, const char *newVal);
CK_VISIBLE_PUBLIC const char *CkImap_sslProtocol(HCkImap cHandle);
CK_VISIBLE_PUBLIC BOOL CkImap_getSslServerCertVerified(HCkImap cHandle);
CK_VISIBLE_PUBLIC BOOL CkImap_getStartTls(HCkImap cHandle);
CK_VISIBLE_PUBLIC void CkImap_putStartTls(HCkImap cHandle, BOOL newVal);
CK_VISIBLE_PUBLIC int CkImap_getUidNext(HCkImap cHandle);
CK_VISIBLE_PUBLIC int CkImap_getUidValidity(HCkImap cHandle);
CK_VISIBLE_PUBLIC BOOL CkImap_getUtf8(HCkImap cHandle);
CK_VISIBLE_PUBLIC void CkImap_putUtf8(HCkImap cHandle, BOOL newVal);
CK_VISIBLE_PUBLIC BOOL CkImap_getVerboseLogging(HCkImap cHandle);
CK_VISIBLE_PUBLIC void CkImap_putVerboseLogging(HCkImap cHandle, BOOL newVal);
CK_VISIBLE_PUBLIC void CkImap_getVersion(HCkImap cHandle, HCkString retval);
CK_VISIBLE_PUBLIC const char *CkImap_version(HCkImap cHandle);
CK_VISIBLE_PUBLIC BOOL CkImap_AddPfxSourceData(HCkImap cHandle, HCkByteData pfxBytes, const char *pfxPassword);
CK_VISIBLE_PUBLIC BOOL CkImap_AddPfxSourceFile(HCkImap cHandle, const char *pfxFilePath, const char *pfxPassword);
CK_VISIBLE_PUBLIC BOOL CkImap_AppendMail(HCkImap cHandle, const char *mailbox, HCkEmail email);
CK_VISIBLE_PUBLIC BOOL CkImap_AppendMime(HCkImap cHandle, const char *mailbox, const char *mimeText);
CK_VISIBLE_PUBLIC BOOL CkImap_AppendMimeWithDate(HCkImap cHandle, const char *mailbox, const char *mimeText, SYSTEMTIME *internalDate);
CK_VISIBLE_PUBLIC BOOL CkImap_AppendMimeWithDateStr(HCkImap cHandle, const char *mailbox, const char *mimeText, const char *internalDateStr);
CK_VISIBLE_PUBLIC BOOL CkImap_AppendMimeWithFlags(HCkImap cHandle, const char *mailbox, const char *mimeText, BOOL seen, BOOL flagged, BOOL answered, BOOL draft);
CK_VISIBLE_PUBLIC BOOL CkImap_Capability(HCkImap cHandle, HCkString outStr);
CK_VISIBLE_PUBLIC const char *CkImap_capability(HCkImap cHandle);
CK_VISIBLE_PUBLIC HCkMessageSet CkImap_CheckForNewEmail(HCkImap cHandle);
CK_VISIBLE_PUBLIC void CkImap_ClearSessionLog(HCkImap cHandle);
CK_VISIBLE_PUBLIC BOOL CkImap_CloseMailbox(HCkImap cHandle, const char *mailbox);
CK_VISIBLE_PUBLIC BOOL CkImap_Connect(HCkImap cHandle, const char *domainName);
CK_VISIBLE_PUBLIC BOOL CkImap_Copy(HCkImap cHandle, int msgId, BOOL bUid, const char *copyToMailbox);
CK_VISIBLE_PUBLIC BOOL CkImap_CopyMultiple(HCkImap cHandle, HCkMessageSet messageSet, const char *copyToMailbox);
CK_VISIBLE_PUBLIC BOOL CkImap_CopySequence(HCkImap cHandle, int startSeqNum, int count, const char *copyToMailbox);
CK_VISIBLE_PUBLIC BOOL CkImap_CreateMailbox(HCkImap cHandle, const char *mailbox);
CK_VISIBLE_PUBLIC BOOL CkImap_DeleteMailbox(HCkImap cHandle, const char *mailbox);
CK_VISIBLE_PUBLIC BOOL CkImap_Disconnect(HCkImap cHandle);
CK_VISIBLE_PUBLIC BOOL CkImap_ExamineMailbox(HCkImap cHandle, const char *mailbox);
CK_VISIBLE_PUBLIC BOOL CkImap_Expunge(HCkImap cHandle);
CK_VISIBLE_PUBLIC BOOL CkImap_ExpungeAndClose(HCkImap cHandle);
CK_VISIBLE_PUBLIC BOOL CkImap_FetchAttachment(HCkImap cHandle, HCkEmail emailObject, int attachmentIndex, const char *saveToPath);
CK_VISIBLE_PUBLIC BOOL CkImap_FetchAttachmentBytes(HCkImap cHandle, HCkEmail email, int attachIndex, HCkByteData outBytes);
CK_VISIBLE_PUBLIC BOOL CkImap_FetchAttachmentString(HCkImap cHandle, HCkEmail emailObject, int attachmentIndex, const char *charset, HCkString outStr);
CK_VISIBLE_PUBLIC const char *CkImap_fetchAttachmentString(HCkImap cHandle, HCkEmail emailObject, int attachmentIndex, const char *charset);
CK_VISIBLE_PUBLIC HCkEmailBundle CkImap_FetchBundle(HCkImap cHandle, HCkMessageSet messageSet);
CK_VISIBLE_PUBLIC HCkStringArray CkImap_FetchBundleAsMime(HCkImap cHandle, HCkMessageSet messageSet);
CK_VISIBLE_PUBLIC HCkEmailBundle CkImap_FetchChunk(HCkImap cHandle, int startSeqNum, int count, HCkMessageSet failedSet, HCkMessageSet fetchedSet);
CK_VISIBLE_PUBLIC BOOL CkImap_FetchFlags(HCkImap cHandle, int msgId, BOOL bUid, HCkString outStrFlags);
CK_VISIBLE_PUBLIC const char *CkImap_fetchFlags(HCkImap cHandle, int msgId, BOOL bUid);
CK_VISIBLE_PUBLIC HCkEmailBundle CkImap_FetchHeaders(HCkImap cHandle, HCkMessageSet messageSet);
CK_VISIBLE_PUBLIC HCkEmailBundle CkImap_FetchSequence(HCkImap cHandle, int startSeqNum, int numMessages);
CK_VISIBLE_PUBLIC HCkStringArray CkImap_FetchSequenceAsMime(HCkImap cHandle, int startSeqNum, int numMessages);
CK_VISIBLE_PUBLIC HCkEmailBundle CkImap_FetchSequenceHeaders(HCkImap cHandle, int startSeqNum, int numMessages);
CK_VISIBLE_PUBLIC HCkEmail CkImap_FetchSingle(HCkImap cHandle, int msgId, BOOL bUid);
CK_VISIBLE_PUBLIC BOOL CkImap_FetchSingleAsMime(HCkImap cHandle, int msgId, BOOL bUid, HCkString outStrMime);
CK_VISIBLE_PUBLIC const char *CkImap_fetchSingleAsMime(HCkImap cHandle, int msgId, BOOL bUid);
CK_VISIBLE_PUBLIC HCkEmail CkImap_FetchSingleHeader(HCkImap cHandle, int msgId, BOOL bUid);
CK_VISIBLE_PUBLIC BOOL CkImap_FetchSingleHeaderAsMime(HCkImap cHandle, int msgId, BOOL bUID, HCkString outStr);
CK_VISIBLE_PUBLIC const char *CkImap_fetchSingleHeaderAsMime(HCkImap cHandle, int msgId, BOOL bUID);
CK_VISIBLE_PUBLIC HCkMessageSet CkImap_GetAllUids(HCkImap cHandle);
CK_VISIBLE_PUBLIC BOOL CkImap_GetMailAttachFilename(HCkImap cHandle, HCkEmail email, int attachIndex, HCkString outStrFilename);
CK_VISIBLE_PUBLIC const char *CkImap_getMailAttachFilename(HCkImap cHandle, HCkEmail email, int attachIndex);
CK_VISIBLE_PUBLIC int CkImap_GetMailAttachSize(HCkImap cHandle, HCkEmail email, int attachIndex);
CK_VISIBLE_PUBLIC int CkImap_GetMailFlag(HCkImap cHandle, HCkEmail email, const char *flagName);
CK_VISIBLE_PUBLIC int CkImap_GetMailNumAttach(HCkImap cHandle, HCkEmail email);
CK_VISIBLE_PUBLIC int CkImap_GetMailSize(HCkImap cHandle, HCkEmail email);
CK_VISIBLE_PUBLIC HCkCert CkImap_GetSslServerCert(HCkImap cHandle);
CK_VISIBLE_PUBLIC BOOL CkImap_IdleCheck(HCkImap cHandle, int timeoutMs, HCkString outStr);
CK_VISIBLE_PUBLIC const char *CkImap_idleCheck(HCkImap cHandle, int timeoutMs);
CK_VISIBLE_PUBLIC BOOL CkImap_IdleDone(HCkImap cHandle);
CK_VISIBLE_PUBLIC BOOL CkImap_IdleStart(HCkImap cHandle);
CK_VISIBLE_PUBLIC BOOL CkImap_IsConnected(HCkImap cHandle);
CK_VISIBLE_PUBLIC BOOL CkImap_IsLoggedIn(HCkImap cHandle);
CK_VISIBLE_PUBLIC BOOL CkImap_IsUnlocked(HCkImap cHandle);
CK_VISIBLE_PUBLIC HCkMailboxes CkImap_ListMailboxes(HCkImap cHandle, const char *reference, const char *wildcardedMailbox);
CK_VISIBLE_PUBLIC HCkMailboxes CkImap_ListSubscribed(HCkImap cHandle, const char *reference, const char *wildcardedMailbox);
CK_VISIBLE_PUBLIC BOOL CkImap_Login(HCkImap cHandle, const char *login, const char *password);
CK_VISIBLE_PUBLIC BOOL CkImap_Logout(HCkImap cHandle);
CK_VISIBLE_PUBLIC BOOL CkImap_Noop(HCkImap cHandle);
CK_VISIBLE_PUBLIC BOOL CkImap_RefetchMailFlags(HCkImap cHandle, HCkEmail email);
CK_VISIBLE_PUBLIC BOOL CkImap_RenameMailbox(HCkImap cHandle, const char *fromMailbox, const char *toMailbox);
CK_VISIBLE_PUBLIC BOOL CkImap_SaveLastError(HCkImap cHandle, const char *path);
CK_VISIBLE_PUBLIC HCkMessageSet CkImap_Search(HCkImap cHandle, const char *criteria, BOOL bUid);
CK_VISIBLE_PUBLIC BOOL CkImap_SelectMailbox(HCkImap cHandle, const char *mailbox);
CK_VISIBLE_PUBLIC BOOL CkImap_SendRawCommand(HCkImap cHandle, const char *cmd, HCkString outRawResponse);
CK_VISIBLE_PUBLIC const char *CkImap_sendRawCommand(HCkImap cHandle, const char *cmd);
CK_VISIBLE_PUBLIC BOOL CkImap_SendRawCommandB(HCkImap cHandle, const char *cmd, HCkByteData outBytes);
CK_VISIBLE_PUBLIC BOOL CkImap_SendRawCommandC(HCkImap cHandle, HCkByteData cmd, HCkByteData outBytes);
#if defined(CK_CSP_INCLUDED)
CK_VISIBLE_PUBLIC BOOL CkImap_SetCSP(HCkImap cHandle, HCkCsp csp);
#endif
CK_VISIBLE_PUBLIC BOOL CkImap_SetDecryptCert(HCkImap cHandle, HCkCert cert);
CK_VISIBLE_PUBLIC BOOL CkImap_SetDecryptCert2(HCkImap cHandle, HCkCert cert, HCkPrivateKey key);
CK_VISIBLE_PUBLIC BOOL CkImap_SetFlag(HCkImap cHandle, int msgId, BOOL bUid, const char *flagName, int value);
CK_VISIBLE_PUBLIC BOOL CkImap_SetFlags(HCkImap cHandle, HCkMessageSet messageSet, const char *flagName, int value);
CK_VISIBLE_PUBLIC BOOL CkImap_SetMailFlag(HCkImap cHandle, HCkEmail email, const char *flagName, int value);
CK_VISIBLE_PUBLIC BOOL CkImap_SetSslClientCert(HCkImap cHandle, HCkCert cert);
CK_VISIBLE_PUBLIC BOOL CkImap_SetSslClientCertPem(HCkImap cHandle, const char *pemDataOrFilename, const char *pemPassword);
CK_VISIBLE_PUBLIC BOOL CkImap_SetSslClientCertPfx(HCkImap cHandle, const char *pfxFilename, const char *pfxPassword);
CK_VISIBLE_PUBLIC BOOL CkImap_SshAuthenticatePk(HCkImap cHandle, const char *sshLogin, HCkSshKey privateKey);
CK_VISIBLE_PUBLIC BOOL CkImap_SshAuthenticatePw(HCkImap cHandle, const char *sshLogin, const char *sshPassword);
CK_VISIBLE_PUBLIC BOOL CkImap_SshTunnel(HCkImap cHandle, const char *sshServerHostname, int sshPort);
CK_VISIBLE_PUBLIC BOOL CkImap_StoreFlags(HCkImap cHandle, int msgId, BOOL bUid, const char *flagNames, int value);
CK_VISIBLE_PUBLIC BOOL CkImap_Subscribe(HCkImap cHandle, const char *mailbox);
CK_VISIBLE_PUBLIC BOOL CkImap_UnlockComponent(HCkImap cHandle, const char *unlockCode);
CK_VISIBLE_PUBLIC BOOL CkImap_Unsubscribe(HCkImap cHandle, const char *mailbox);
CK_VISIBLE_PUBLIC BOOL CkImap_UseCertVault(HCkImap cHandle, HCkXmlCertVault vault);
#endif
