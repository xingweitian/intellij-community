/*
 * Copyright 2000-2009 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.intellij.codeInsight.intention.actions;

import com.intellij.codeInsight.actions.BaseCodeInsightAction;
import com.intellij.codeInsight.hint.HintManagerImpl;
import com.intellij.codeInsight.intention.impl.ShowIntentionActionsHandler;
import com.intellij.ide.lightEdit.LightEdit;
import com.intellij.ide.lightEdit.LightEditCompatible;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.application.ApplicationBundle;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilBase;
import org.jetbrains.annotations.NotNull;

/**
 * @author mike
 */
public class ShowIntentionActionsAction extends BaseCodeInsightAction implements HintManagerImpl.ActionToIgnore,
                                                                                 LightEditCompatible,
                                                                                 DumbAware {
  public ShowIntentionActionsAction() {
    setEnabledInModalContext(true);
  }

  @Override
  public void update(@NotNull AnActionEvent event) {
    Project project = event.getProject();
    Presentation presentation = event.getPresentation();
    if (LightEdit.owns(project)) {
      presentation.setEnabledAndVisible(true);
      return;
    }
    super.update(event);
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    Project project = e.getProject();
    if (project == null) return;

    if (!LightEdit.owns(project) && DumbService.isDumb(project)) {
      DumbService.getInstance(project).showDumbModeNotification(
        ApplicationBundle.message("intentions.are.not.available.message"));
      return;
    }

    Editor editor = getEditor(e.getDataContext(), project, false);
    if (editor == null) return;

    final PsiFile psiFile = PsiUtilBase.getPsiFileInEditor(editor, project);
    if (psiFile == null) return;

    if (!ApplicationManager.getApplication().isUnitTestMode() && !editor.getContentComponent().isShowing()) return;
    getHandler().invoke(project, editor, psiFile, e.isFromContextMenu());
  }

  @Override
  protected boolean isValidForLookup() {
    return true;
  }

  @NotNull
  @Override
  protected ShowIntentionActionsHandler getHandler() {
    return new ShowIntentionActionsHandler();
  }
}
