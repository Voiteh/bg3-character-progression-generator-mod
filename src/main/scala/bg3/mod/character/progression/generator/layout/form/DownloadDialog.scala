package bg3.mod.character.progression.generator.layout.form

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.html.Anchor
import com.vaadin.flow.component.icon.{Icon, VaadinIcon}
import com.vaadin.flow.component.orderedlayout.VerticalLayout

import java.net.URL


class DownloadDialog(url: URL) extends Dialog {
  setHeaderTitle("Download generated progression")
  val downloadAnchor = new Anchor(url.toString)
  downloadAnchor.getElement.setAttribute("download", true)
  downloadAnchor.removeAll()
  val download = new Button("Download progression", new Icon(VaadinIcon.DOWNLOAD_ALT))
  downloadAnchor.add(download)

  val cancelButton = new Button("Cancel", e => close())
  getFooter.add(cancelButton)
  getFooter.add(downloadAnchor)
}
